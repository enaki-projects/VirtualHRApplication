import {Request, Response, Router} from 'express';
import config from "../../config";
import services from "../../services";

const route = Router();
import { check, validationResult } from 'express-validator';


export default (app: Router) => {
    app.use(route);

    route.get('/login', (req: Request, res: Response) => {
        return res.render('_login.ejs', {
            enable_login_css: true
        });
    });

    route.post('/login',
        [
            check("email", "Please enter a valid email").isEmail(),
            check("password", "Please enter a valid password")
        ],
        async (req: Request, res: Response) => {
            const errors = validationResult(req);
            if (!errors.isEmpty()) {
                return res.status(400).json({
                    errors: errors.array()
                });
            }
            const url = config.adapterUrl + "api/auth/login?as=administrator";
            const settings = {
                method: 'post',
                body:    JSON.stringify({
                    "email": req.body.email,
                    "password": req.body.password
                }),
                headers: { 'Content-Type': 'application/json' },
            };
            const response = await fetch(url, settings)
            if (response["status"] === 200){
                req.session.currentUser = await response.json();

                const url = config.adapterUrl + "api/cvs/" + req.session.currentUser["uid"];
                const settings = {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json',
                        "Authorization": "Bearer " + req.session.currentUser["token"]
                    },
                };
                req.session.currentUser["cv"] = await((await fetch(url, settings)).json());
                req.session.currentUser["cv"]["photoUrl"] = services.getUrlPhoto(req.session.currentUser)
                req.session.currentUser["cv"]["age"] = services.getAge(req.session.currentUser["cv"]["birthdate"])
                res.redirect(config.basePath + "/");
            }
            else {
                res.sendStatus(response["status"]);
            }
    });

    app.use((req, res, next) => {       // Catches access to all other pages
        if(!req.session.currentUser) {       // requiring a valid access token
            res.redirect(config.basePath + '/login');
        } else {
            next();
        }
    });
};
