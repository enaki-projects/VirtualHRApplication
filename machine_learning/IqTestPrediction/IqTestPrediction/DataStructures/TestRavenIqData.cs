using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IqTestPrediction.DataStructures
{
    public static class RavenIqSample
    {
        internal static readonly RavenIqData testRavenIqData = new RavenIqData
        {
            NumberOfCorrectAnswers = 60,
            A = 12,
            B = 12,
            C = 12,
            D = 12,
            E = 12,
            IQ = 0
        };

        public static IEnumerable<RavenIqData> GenerateSampleData(int minRangeValue, int maxRangeValue)
        {
            var ravenIqDatas = new List<RavenIqData>();
            for (int A = minRangeValue; A < maxRangeValue; A++)
            {
                for (int B = minRangeValue; B < maxRangeValue; B++)
                {
                    for (int C = minRangeValue; C < maxRangeValue; C++)
                    {
                        for (int D = minRangeValue; D < maxRangeValue; D++)
                        {
                            for (int E = minRangeValue; E < maxRangeValue; E++)
                            {
                                ravenIqDatas.Add(new RavenIqData()
                                {
                                    NumberOfCorrectAnswers = A + B + C + D + E,
                                    A = A,
                                    B = B,
                                    C = C,
                                    D = D,
                                    E = E,
                                    IQ = 0
                                });
                            }
                        }
                    }
                }
            }
            return ravenIqDatas;
        }
    };
}
