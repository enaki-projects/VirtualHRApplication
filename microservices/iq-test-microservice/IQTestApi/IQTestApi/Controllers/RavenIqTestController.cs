using System.Threading.Tasks;
using System.Web.Http;
using IQTestServices.Interfaces;
using IQTestServices.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace IQTestApi.Controllers
{
    [ApiController]
    [Microsoft.AspNetCore.Mvc.Route("api/raven-test")]
    public class RavenIqTestController : ControllerBase
    {
        private readonly ILogger<RavenIqTestController> _logger;
        private readonly IRavenIqTestService _ravenIqTestService;

        public RavenIqTestController(ILogger<RavenIqTestController> logger, IRavenIqTestService ravenIqTestService)
        {
            _logger = logger;
            _ravenIqTestService = ravenIqTestService;
        }

        [Microsoft.AspNetCore.Mvc.HttpGet]
        public async Task<IActionResult> Get([FromUri] bool randomize = false)
        {
            var result = await _ravenIqTestService.GetRavenQuestions(randomize);
            return Ok(result);
        }

        [Microsoft.AspNetCore.Mvc.HttpGet("{questionCode}")]
        public async Task<IActionResult> GetQuestion([FromRoute] string questionCode)
        {
            var bytes = await _ravenIqTestService.GetQuestion(questionCode);
            if (bytes != null) return File(bytes, "image/png");

            return NotFound();
        }

        [Microsoft.AspNetCore.Mvc.HttpPost]
        public async Task<IActionResult> ProcessTest([Microsoft.AspNetCore.Mvc.FromBody] RavenIqTest test)
        {
            var result = await _ravenIqTestService.ProcessRavenTest(test);
            return Ok(result);
        }
    }
}