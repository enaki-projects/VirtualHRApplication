using Microsoft.ML.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IqTestPrediction.DataStructures
{
    public class RavenIqPrediction
    {
        [ColumnName("Score")]
        public float IQ;
    }
}
