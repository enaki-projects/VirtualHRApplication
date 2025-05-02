import config from './config';
import express from 'express';
import Logger from './loaders/logger';

async function startServer() {
    const app = express();

    await require('./loaders').default({ expressApp: app });

    app.listen(config.port, () => {
        Logger.info(`
      ################################################
      🛡️  Server listening on port: ${config.port} 🛡️
      ################################################
    `);
        Logger.info(`http://localhost:${config.port}/home`)
        Logger.info(`Adapter: ${config.adapterIP}`)
    });

}

startServer();
