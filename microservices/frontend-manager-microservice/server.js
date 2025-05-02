//Install express server
const express = require('express');
const path = require('path');

const app = express();
// Serve only the static files form the dist directory
app.use(express.static(__dirname + '/build'));

const winston = require('winston')
const consoleTransport = new winston.transports.Console({
  format: winston.format.combine(
    winston.format.cli(),
    winston.format.splat()
  )
})

const myWinstonOptions = {
  level: process.env.LOG_LEVEL || 'silly',
  levels: winston.config.npm.levels,
  format: winston.format.combine(
    winston.format.timestamp({
      format: 'YYYY-MM-DD HH:mm:ss'
    }),
    winston.format.errors({ stack: true }),
    winston.format.splat(),
    winston.format.json()
  ),
  transports: [consoleTransport]
}
const logger = new winston.createLogger(myWinstonOptions)

function logRequest(req, res, next) {
  logger.info(req.url)
  next()
}

app.use(logRequest)

app.get('/*', function(req,res) {
  res.sendFile(path.join(__dirname+'/build/index.html'));
});

// Start the app by listening on the default Heroku port
app.listen(process.env.PORT || 3001, '0.0.0.0',() => {
  logger.info(`
      ################################################
      🛡️  Server listening on port: ${process.env.PORT || 8080} 🛡️
      ################################################
    `);
  logger.info(process.env.NODE_ENV);
});
