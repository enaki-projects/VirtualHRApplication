import express, {NextFunction, Request} from 'express';
import bodyParser from 'body-parser';
import routes from '../api';
import config from '../config';

const session = require('express-session');

const expressLayouts = require('express-ejs-layouts');

export default ({ app }: { app: express.Application }) => {
    app.get('/status', (req, res) => {
        res.status(200).end();
    });
    app.head('/status', (req, res) => {
        res.status(200).end();
    });

    app.enable('trust proxy');
    app.use(bodyParser.urlencoded({extended:true}));
    app.use(bodyParser.json());
    app.use(session({
        key: 'user_sid',
        secret: 'secret',
        saveUninitialized: false,
    }));

    app.use(express.static("public"));
    app.set('view engine', 'ejs');
    app.set('views', 'views'); // general config
    app.set('view options', { layout:'layout.ejs' });
    app.use(expressLayouts);

    app.use(config.api.prefix, routes());


    /// catch 404 and forward to error handler
    app.use((req, res, next) => {
        const err = new Error('Not Found');
        // @ts-ignore
        err.status = 404;
        next(err);
    });

    /// error handlers
    app.use((err, req, res, next) => {
        /* Handle 401 thrown by express-jwt library */
        if (err.name === 'UnauthorizedError') {
            return res
                .status(err.status)
                .send({ message: err.message })
                .end();
        }
        return next(err);
    });

    app.use((err, req, res, next) => {
        res.status(err.status || 500);
        res.json({
            errors: {
                message: err.message,
            },
        });
    });
};
