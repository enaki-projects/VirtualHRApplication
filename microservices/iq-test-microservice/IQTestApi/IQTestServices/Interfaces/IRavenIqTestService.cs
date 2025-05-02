using System.Collections.Generic;
using System.Threading.Tasks;
using IQTestServices.Models;

namespace IQTestServices.Interfaces
{
    public interface IRavenIqTestService
    {
        public string GetMessage();
        Task<Dictionary<string, RavenIqQuestions>> GetRavenQuestions(bool randomize);
        Task<byte[]> GetQuestion(string questionCode);
        Task<RavenIqResult> ProcessRavenTest(RavenIqTest test);
    }
}