namespace IQTestServices.Models
{
    public class RavenIqResult
    {
        public string Diagnostic;
        public float ExactIQ;
        public int IQ;
        public int IQAdjustedByAge;
        public string TestName = "Raven’s Advanced Progressive Matrices";
        public int TimeTakenInSeconds;

        public RavenIqResult(float exactIq, int iq, int iqAdjustedByAge, int timeTakenInSeconds, string diagnostic)
        {
            ExactIQ = exactIq;
            IQ = iq;
            IQAdjustedByAge = iqAdjustedByAge;
            TimeTakenInSeconds = timeTakenInSeconds;
            Diagnostic = diagnostic;
        }
    }
}