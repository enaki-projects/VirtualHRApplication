import {Request, Response, Router} from 'express';
import config from '../../config';

const route = Router();

export default (app: Router) => {
    app.use(route);

    route.get('/logout', (req: Request, res: Response) => {
        if (req.session.currentUser) {   //if logged
            console.log("User session [Log-OUT]: ", req.session.currentUser);
            req.session.currentUser = undefined;
        }
        res.redirect(config.basePath + "/login");
    });

    app.use((req, res, next) => {       // Catches access to all other pages
        if(!req.session.currentUser) {       // requiring a valid access token
            res.redirect(config.basePath + '/login');
        } else {
            next();
        }
    });
};
