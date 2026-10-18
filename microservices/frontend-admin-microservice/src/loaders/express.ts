import express, { NextFunction, Request, Response } from 'express';
import session from 'express-session';
import routes from '../api';
import config from '../config';
import Logger from './logger';

// eslint-disable-next-line @typescript-eslint/no-require-imports
const expressLayouts = require('express-ejs-layouts');

export default ({ app }: { app: express.Application }) => {
    app.get('/status', (_req, res) => {
        res.status(200).end();
    });

    if (config.sessionSecret === 'dev-only-change-me') {
        Logger.warn('SESSION_SECRET is not set; using an insecure development secret.');
    }

    app.set('trust proxy', true);
    app.set('view engine', 'ejs');
    app.set('views', 'views');
    app.set('view options', { layout: 'layout.ejs' });
    // Exposed to every view so links, assets and forms work under BASE_PATH.
    app.locals.base = config.basePath;

    const site = express.Router();
    site.use(express.static('public'));
    site.use(express.urlencoded({ extended: true }));
    site.use(express.json());
    site.use(session({
        name: 'user_sid',
        secret: config.sessionSecret,
        resave: false,
        saveUninitialized: false,
        cookie: {
            path: config.basePath || '/',
            httpOnly: true,
            sameSite: 'lax',
            secure: 'auto',
        },
    }));
    site.use(expressLayouts);
    site.use(routes());

    app.use(config.basePath || '/', site);

    app.use((_req: Request, _res: Response, next: NextFunction) => {
        const err: Error & { status?: number } = new Error('Not Found');
        err.status = 404;
        next(err);
    });

    app.use((err: Error & { status?: number }, _req: Request, res: Response, _next: NextFunction) => {
        res.status(err.status || 500);
        res.json({ errors: { message: err.message } });
    });
};
