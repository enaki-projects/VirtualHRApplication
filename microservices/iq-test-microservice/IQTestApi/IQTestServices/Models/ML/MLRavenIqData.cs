using Microsoft.ML.Data;

namespace IQTestServices.Models.ML
{
    public class MlRavenIqData
    {
        [LoadColumn(1)] public float A;

        [LoadColumn(2)] public float B;

        [LoadColumn(3)] public float C;

        [LoadColumn(4)] public float D;

        [LoadColumn(5)] public float E;

        [LoadColumn(6)] public float IQ;

        [LoadColumn(0)] public float NumberOfCorrectAnswers;

        public float GetNumberOfCorrectAnswers()
        {
            return A + B + C + D + E;
        }
    }
}