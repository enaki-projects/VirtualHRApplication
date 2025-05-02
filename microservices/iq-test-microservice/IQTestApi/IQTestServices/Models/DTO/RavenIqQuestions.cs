using System.Collections.Generic;

namespace IQTestServices.Models
{
    public class RavenIqQuestions
    {
        public IList<string> Questions;
        public int Total;

        public RavenIqQuestions(IList<string> questions, int total)
        {
            Questions = questions;
            Total = total;
        }
    }
}