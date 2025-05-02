using Microsoft.ML.Data;

namespace IQTestServices.Models.ML
{
    public class MlRavenIqPrediction
    {
        [ColumnName("Score")] public float IQ;
    }
}