import {Request, Response, Router} from 'express';
import config from '../../config';

const route = Router();
const fetch = require('node-fetch');

export default (app: Router) => {
    app.use(route);

    route.get('/cv', (req: Request, res: Response) => {
        return res.render('_cv.ejs', {
            javascript_file: '_cv.js',
            currentUser: req["session"]["currentUser"]
        });
    });

    route.get('/cv-data', (req: Request, res: Response) => {
        const params = req.query;
        const url = new URL(config.adapterIP + "cv");
        Object.keys(params).forEach(key => url.searchParams.append(key, params[key] as string));

        console.log(params)

        const settings = { method: "Get" };
        fetch(url, settings, 2000)
            .then((res: Response) => res.json())
            .then((json: JSON) => {
                return res.json(json)
            })
            .catch((e: Error) => {
                console.log(e.stack);
                return res.json({});
            });
    });

    route.post('/cv', (req: Request, res: Response) => {
        // console.log(req);
        return res.render('_cv.ejs');
    });
};
