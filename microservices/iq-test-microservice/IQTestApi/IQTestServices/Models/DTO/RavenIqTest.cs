using System;
using System.Collections.Generic;

namespace IQTestServices.Models
{
    public class RavenIqTest
    {
        public int Age = 0;
        public IList<RavenIqAnswer> Answers;
        public DateTimeOffset Finished;
        public DateTimeOffset Started;
    }
}