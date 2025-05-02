using Microsoft.ML.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IqTestPrediction.DataStructures
{
    public class RavenIqData
    {
        [LoadColumn(0)]
        public float NumberOfCorrectAnswers;

        [LoadColumn(1)]
        public float A;

        [LoadColumn(2)]
        public float B;

        [LoadColumn(3)]
        public float C;

        [LoadColumn(4)]
        public float D;

        [LoadColumn(5)]
        public float E;

        [LoadColumn(6)]
        public float IQ;
    }
}
