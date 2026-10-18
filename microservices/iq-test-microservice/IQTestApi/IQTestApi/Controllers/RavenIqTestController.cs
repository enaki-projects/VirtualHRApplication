using System.Threading.Tasks;
using IQTestServices.Interfaces;
using IQTestServices.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace IQTestApi.Controllers
{
    [ApiController]
    [Route("api/raven-test")]
    public class RavenIqTestController : ControllerBase
    {
        private readonly ILogger<RavenIqTestController> _logger;
        private readonly IRavenIqTestService _ravenIqTestService;

        public RavenIqTestController(ILogger<RavenIqTestController> logger, IRavenIqTestService ravenIqTestService)
        {
            _logger = logger;
            _ravenIqTestService = ravenIqTestService;
        }

        [HttpGet]
        public async Task<IActionResult> Get([FromQuery] bool randomize = false)
        {
            var result = await _ravenIqTestService.GetRavenQuestions(randomize);
            return Ok(result);
        }

        [HttpGet("{questionCode}")]
        public async Task<IActionResult> GetQuestion([FromRoute] string questionCode)
        {
            var bytes = await _ravenIqTestService.GetQuestion(questionCode);
            if (bytes != null) return File(bytes, "image/png");

            return NotFound();
        }

        [HttpPost]
        public async Task<IActionResult> ProcessTest([FromBody] RavenIqTest test)
        {
            var result = await _ravenIqTestService.ProcessRavenTest(test);
            return Ok(result);
        }
    }
}