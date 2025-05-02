using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using IQTestServices.Helpers;
using IQTestServices.Interfaces;
using IQTestServices.ML;
using IQTestServices.Models;
using IQTestServices.Models.ML;
using Microsoft.ML;
using Newtonsoft.Json;

namespace IQTestServices.Services
{
    public class RavenIqTestService : IRavenIqTestService
    {
        //LOCALHOST
        //private const string ResourcePath = "../_Resources/AdvancedRaven";
        
        //Pentru Docker
        private const string ResourcePath = "AdvancedRaven";
        
        private readonly MLModel _mlModel;
        private readonly Dictionary<string, RavenIqCorrectAnswers> _ravenAnswers;
        private readonly Dictionary<string, RavenIqQuestions> _ravenQuestions;
        private readonly Regex _rgx;

        public RavenIqTestService()
        {
            var mlContext = new MLContext(0);
            _mlModel = new MLModel(mlContext, Utils.ModelPath);

            _rgx = new Regex(@"^[A-E]-([1-9]|1[0-2])$"); // A-1 .. E-12 
            using (var file = File.OpenText($"{ResourcePath}/AdvancedRavenQuestions.json"))
            {
                var serializer = new JsonSerializer();
                _ravenQuestions =
                    (Dictionary<string, RavenIqQuestions>) serializer.Deserialize(file,
                        typeof(Dictionary<string, RavenIqQuestions>));
            }

            using (var file = File.OpenText($"{ResourcePath}/AdvancedRavenAnswers.json"))
            {
                var serializer = new JsonSerializer();
                _ravenAnswers =
                    (Dictionary<string, RavenIqCorrectAnswers>) serializer.Deserialize(file,
                        typeof(Dictionary<string, RavenIqCorrectAnswers>));
            }
        }

        public string GetMessage()
        {
            return "Hello world!";
        }

        public async Task<Dictionary<string, RavenIqQuestions>> GetRavenQuestions(bool randomize)
        {
            if (!randomize) return await Task.FromResult(_ravenQuestions);
            // Randomize
            var copyRavenIqQuestions = new Dictionary<string, RavenIqQuestions>(_ravenQuestions);

            foreach (var item in copyRavenIqQuestions.Values)
                item.Questions = item.Questions.OrderBy(x => Guid.NewGuid()).ToList();
            return await Task.FromResult(copyRavenIqQuestions);
        }

        public async Task<byte[]> GetQuestion(string questionCode)
        {
            if (!ValidateQuestionCode(questionCode)) return null;
            var bytes = await File.ReadAllBytesAsync($"{ResourcePath}/{questionCode[0]}/{questionCode}.png");
            return bytes;
        }

        public async Task<RavenIqResult> ProcessRavenTest(RavenIqTest test)
        {
            var difference = test.Finished - test.Started;

            var testAnswersDict = new Dictionary<string, int>();
            foreach (var ravenIqAnswer in test.Answers) testAnswersDict[ravenIqAnswer.Question] = ravenIqAnswer.Chosen;

            var mlRavenIqData = new MlRavenIqData();
            var mlRavenIqDataDict = new Dictionary<string, int>
            {
                ["A"] = 0,
                ["B"] = 0,
                ["C"] = 0,
                ["D"] = 0,
                ["E"] = 0
            };
            foreach (var (question, chosen) in testAnswersDict)
            {
                if (!ValidateQuestionCode(question)) continue;

                var correctAnswer = _ravenAnswers[question[0].ToString()]
                    .Questions[question];

                if (correctAnswer == chosen) mlRavenIqDataDict[question[0].ToString()] += 1;
            }

            mlRavenIqData.A = mlRavenIqDataDict["A"];
            mlRavenIqData.B = mlRavenIqDataDict["B"];
            mlRavenIqData.C = mlRavenIqDataDict["C"];
            mlRavenIqData.D = mlRavenIqDataDict["D"];
            mlRavenIqData.E = mlRavenIqDataDict["E"];
            mlRavenIqData.NumberOfCorrectAnswers = mlRavenIqData.GetNumberOfCorrectAnswers();

            var exactIq = _mlModel.Predict(mlRavenIqData).IQ;
            var iq = (int) Math.Round(exactIq);
            var iqAdjustedByAge = (int) Math.Round(IqAgeAdjust(exactIq, test.Age));
            var timeTakenInSeconds = difference.Minutes * 60 + difference.Seconds;
            var diagnostic = GetDiagnostic(iq);
            return await Task.FromResult(
                new RavenIqResult(exactIq, iq, iqAdjustedByAge, timeTakenInSeconds, diagnostic));
        }

        private float IqAgeAdjust(float iq, int age)
        {
            var adjuster = age switch
            {
                <= 30 => 1.0,
                > 30 and <= 35 => 0.97,
                <= 40 => 0.93,
                <= 45 => 0.88,
                <= 50 => 0.82,
                <= 55 => 0.76,
                <= 60 => 0.70,
                _ => 1.0
            };
            return (float) (iq / adjuster);
        }

        private string GetDiagnostic(float iq)
        {
            var diagnostic = iq switch
            {
                <= 20 => "Severe Dementia",
                <= 50 => "The Average Degree of Dementia",
                <= 70 => "Light Degree of Dementia",
                <= 80 => "A Low Level of Intelligence",
                <= 90 => "Below Average Intelligence",
                <= 110 => "Average Level of Intelligence",
                <= 120 => "Above Average Intelligence",
                <= 140 => "High Level of Intelligence",
                _ => "Extraordinary, Outstanding Intelligence"
            };
            return diagnostic;
        }

        private bool ValidateQuestionCode(string questionCode)
        {
            return _rgx.IsMatch(questionCode);
        }
    }
}