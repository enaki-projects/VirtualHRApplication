using Helpers;
using IqTestPrediction.DataStructures;
using IqTestPrediction.Helpers;
using Microsoft.ML;
using Microsoft.ML.Data;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.IO;
using System.Linq;

namespace IqTestPrediction
{
    internal static class Program
    {
        private static string AppPath => Path.GetDirectoryName(Environment.GetCommandLineArgs()[0]);
        private static string BaseDatasetsRelativePath = @"../../../../Data";
        private static string TrainDataRelativePath = $"{BaseDatasetsRelativePath}/raven.csv";
        private static string TestDataRelativePath = $"{BaseDatasetsRelativePath}/raven.csv";

        private static string TrainDataPath = Utils.GetAbsolutePath(TrainDataRelativePath);
        private static string TestDataPath = Utils.GetAbsolutePath(TestDataRelativePath);

        private static string BaseModelsRelativePath = @"../../../../MLModels";
        private static string ModelRelativePath = $"{BaseModelsRelativePath}/RavenAdvancedMatrices.zip";

        private static string ModelPath = Utils.GetAbsolutePath(ModelRelativePath);

        static void Main(string[] args) //If args[0] == "svg" a vector-based chart will be created instead a .png chart
        {
            //Create ML Context with seed for repeatable/deterministic results
            MLContext mlContext = new MLContext(seed: 0);

            // Create, Train, Evaluate and Save a model
            ML.MLPipeline.BuildTrainEvaluateAndSaveModel(mlContext, TrainDataPath, TestDataPath, ModelPath);

            // Make a single test prediction loding the model from .ZIP file
            ML.MLPipeline.TestMultiplePrediction(mlContext, ModelPath, TestDataPath);
            ML.MLPipeline.TestGeneratedPrediction(mlContext, ModelPath, 0, 1);
            ML.MLPipeline.TestGeneratedPrediction(mlContext, ModelPath, 12, 13);

            Console.WriteLine("Press any key to exit..");
            Console.ReadLine();
        }
    }
}
