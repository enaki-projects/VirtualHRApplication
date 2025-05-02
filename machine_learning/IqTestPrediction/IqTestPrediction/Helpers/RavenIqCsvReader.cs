using IqTestPrediction.DataStructures;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IqTestPrediction.Helpers
{
    public class RavenIqCsvReader
    {
        public IEnumerable<RavenIqData> GetDataFromCsv(string dataLocation, int numMaxRecords)
        {
            IEnumerable<RavenIqData> records =
                File.ReadAllLines(dataLocation)
                .Skip(1)
                .Select(x => x.Split(','))
                .Select(x => new RavenIqData()
                {
                    NumberOfCorrectAnswers = float.Parse(x[0], CultureInfo.InvariantCulture),
                    A = float.Parse(x[1], CultureInfo.InvariantCulture),
                    B = float.Parse(x[2], CultureInfo.InvariantCulture),
                    C = float.Parse(x[3], CultureInfo.InvariantCulture),
                    D = float.Parse(x[4], CultureInfo.InvariantCulture),
                    E = float.Parse(x[5], CultureInfo.InvariantCulture),
                    IQ = float.Parse(x[6], CultureInfo.InvariantCulture)
                })
                .Take<RavenIqData>(numMaxRecords);

            return records;
        }
    }
}
