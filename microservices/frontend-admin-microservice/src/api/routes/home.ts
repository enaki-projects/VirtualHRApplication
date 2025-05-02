import {Request, Response, Router} from 'express';
import services from '../../services';
import config from "../../config";

const route = Router();
const fetch = require('node-fetch');

export default (app: Router) => {
    app.use(route);

    route.get('/home', async (req: Request, res: Response) => {

        const url = config.adapterIP + "api/users";
        const settings = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json',
                "Authorization": "Bearer " + req["session"]["currentUser"]["token"]
            },
        };
        const users = await((await fetch(url, settings)).json());
        for (let i = 0; i < users.length; i++){
            users[i]["rolesStr"] = services.getRoles(users[i].roles);
        }
        return res.render('_home.ejs', {
            users: users,
            currentUser: req["session"]["currentUser"],
            enable_home_css: true
        });
    });

    route.get('/', (req: Request, res: Response) => {
        return res.redirect('/home');
    });
};
