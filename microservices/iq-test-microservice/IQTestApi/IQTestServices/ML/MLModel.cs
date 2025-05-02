using IQTestServices.Models.ML;
using Microsoft.ML;

namespace IQTestServices.ML
{
    public class MLModel
    {
        private readonly PredictionEngine<MlRavenIqData, MlRavenIqPrediction> _predEngine;

        public MLModel(MLContext mlContext, string modelPath)
        {
            var trainedModel = mlContext.Model.Load(modelPath, out var modelInputSchema);

            // Create prediction engine related to the loaded trained model
            _predEngine = mlContext.Model.CreatePredictionEngine<MlRavenIqData, MlRavenIqPrediction>(trainedModel);
        }

        public MlRavenIqPrediction Predict(MlRavenIqData ravenIqSample)
        {
            return _predEngine.Predict(ravenIqSample);
        }
    }
}