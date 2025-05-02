using Helpers;
using IqTestPrediction.DataStructures;
using IqTestPrediction.Helpers;
using Microsoft.ML;
using Microsoft.ML.Data;
using System;
using System.Linq;

namespace IqTestPrediction.ML
{
    public static class MLPipeline
    {
        public static ITransformer BuildTrainEvaluateAndSaveModel(MLContext mlContext, string TrainDataPath, string TestDataPath, string ModelPath)
        {
            // STEP 1: Common data loading configuration
            IDataView baseTrainingDataView = mlContext.Data.LoadFromTextFile<RavenIqData>(TrainDataPath, hasHeader: true, separatorChar: ',');
            IDataView testDataView = mlContext.Data.LoadFromTextFile<RavenIqData>(TestDataPath, hasHeader: true, separatorChar: ',');

            //Sample code of removing extreme data like "outliers" for IQ higher than 150 and lower than 50 which can be error-data 
            var cnt = baseTrainingDataView.GetColumn<float>(nameof(RavenIqData.IQ)).Count();
            IDataView trainingDataView = mlContext.Data.FilterRowsByColumn(baseTrainingDataView, nameof(RavenIqData.IQ), lowerBound: 50, upperBound: 150);
            //IDataView trainingDataView = baseTrainingDataView;
            var cnt2 = trainingDataView.GetColumn<float>(nameof(RavenIqData.IQ)).Count();

            // STEP 2: Common data process configuration with pipeline data transformations
            var dataProcessPipeline = mlContext.Transforms.CopyColumns(outputColumnName: "Label", inputColumnName: nameof(RavenIqData.IQ))
                            //.Append(mlContext.Transforms.NormalizeMeanVariance(outputColumnName: nameof(RavenIqData.NumberOfCorrectAnswers)))
                            .Append(mlContext.Transforms.NormalizeMeanVariance(outputColumnName: nameof(RavenIqData.A)))
                            .Append(mlContext.Transforms.NormalizeMeanVariance(outputColumnName: nameof(RavenIqData.B)))
                            .Append(mlContext.Transforms.NormalizeMeanVariance(outputColumnName: nameof(RavenIqData.C)))
                            .Append(mlContext.Transforms.NormalizeMeanVariance(outputColumnName: nameof(RavenIqData.D)))
                            .Append(mlContext.Transforms.NormalizeMeanVariance(outputColumnName: nameof(RavenIqData.E)))
                            .Append(mlContext.Transforms.Concatenate("Features",
                                //nameof(RavenIqData.NumberOfCorrectAnswers),
                                nameof(RavenIqData.A),
                                nameof(RavenIqData.B),
                                nameof(RavenIqData.C),
                                nameof(RavenIqData.D),
                                nameof(RavenIqData.E)));

            // (OPTIONAL) Peek data (such as 5 records) in training DataView after applying the ProcessPipeline's transformations into "Features" 
            ConsoleHelper.PeekDataViewInConsole(mlContext, trainingDataView, dataProcessPipeline, 5);
            ConsoleHelper.PeekVectorColumnDataInConsole(mlContext, "Features", trainingDataView, dataProcessPipeline, 5);

            // STEP 3: Set the training algorithm, then create and config the modelBuilder - Selected Trainer (SDCA Regression algorithm)                            
            var trainer = mlContext.Regression.Trainers.LbfgsPoissonRegression(labelColumnName: "Label", featureColumnName: "Features");
            var trainingPipeline = dataProcessPipeline.Append(trainer);

            // STEP 4: Train the model fitting to the DataSet
            //The pipeline is trained on the dataset that has been loaded and transformed.
            Console.WriteLine("=============== Training the model ===============");
            var trainedModel = trainingPipeline.Fit(trainingDataView);

            // STEP 5: Evaluate the model and show accuracy stats
            Console.WriteLine("===== Evaluating Model's accuracy with Test data =====");

            IDataView predictions = trainedModel.Transform(testDataView);
            var metrics = mlContext.Regression.Evaluate(predictions, labelColumnName: "Label", scoreColumnName: "Score");

            ConsoleHelper.PrintRegressionMetrics(trainer.ToString(), metrics);

            // STEP 6: Save/persist the trained model to a .ZIP file
            mlContext.Model.Save(trainedModel, trainingDataView.Schema, ModelPath);

            Console.WriteLine("The model is saved to {0}", ModelPath);

            return trainedModel;
        }

        public static void TestMultiplePrediction(MLContext mlContext, string ModelPath, string dataLocation)
        {
            ///
            ITransformer trainedModel = mlContext.Model.Load(ModelPath, out var modelInputSchema);

            // Create prediction engine related to the loaded trained model
            var predEngine = mlContext.Model.CreatePredictionEngine<RavenIqData, RavenIqPrediction>(trainedModel);
            
            //Samples: 
            RavenIqCsvReader ravenIqCsvReader = new RavenIqCsvReader();
            var testData = ravenIqCsvReader.GetDataFromCsv(dataLocation, 100);
            foreach (var ravenIqSample in testData)
            {
                //Score
                var resultprediction = predEngine.Predict(ravenIqSample);
                ///
                Console.WriteLine($"->(A, B, C, D, E): ({ravenIqSample.A,2},{ravenIqSample.B,2},{ravenIqSample.C,2},{ravenIqSample.D,2},{ravenIqSample.E,2}) Predicted IQ: {resultprediction.IQ.ToString("F3"),6}, actual IQ: {ravenIqSample.IQ,3}");
            }
        }

        public static void TestGeneratedPrediction(MLContext mlContext, string ModelPath, int min, int max)
        {
            ///
            ITransformer trainedModel = mlContext.Model.Load(ModelPath, out var modelInputSchema);

            // Create prediction engine related to the loaded trained model
            var predEngine = mlContext.Model.CreatePredictionEngine<RavenIqData, RavenIqPrediction>(trainedModel);

            //Samples: 
            RavenIqCsvReader ravenIqCsvReader = new RavenIqCsvReader();
            var testData = RavenIqSample.GenerateSampleData(min, max);
            foreach (var ravenIqSample in testData)
            {
                //Score
                var resultprediction = predEngine.Predict(ravenIqSample);
                ///
                Console.WriteLine($"->(A, B, C, D, E): ({ravenIqSample.A,2},{ravenIqSample.B,2},{ravenIqSample.C,2},{ravenIqSample.D,2},{ravenIqSample.E,2}) Predicted IQ: {resultprediction.IQ.ToString("F3"),6}, actual IQ: ?");
            }
        }
    }
}
