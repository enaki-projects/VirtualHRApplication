import {Request, Response, Router} from 'express';
import services from '../../services';
import config from "../../config";

const route = Router();
const fetch = require('node-fetch');

export default (app: Router) => {
    app.use(route);

    route.get('/users/:id', async (req: Request, res: Response) => {
        console.log(req.params.id);
        const url = config.adapterIP + "api/users/" + req.params.id;
        const settings = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + req["session"]["currentUser"]["token"]
            },
        };
        const user = await ((await fetch(url, settings)).json());
        user["rolesStr"] = services.getRoles(user.roles);
        user["cv"]["photoUrl"] = services.getUrlPhoto(user)
        user["cv"]["age"] = services.getAge(user["cv"]["birthdate"])
        console.log(user);
        return res.render('_users.ejs', {
            currentUser: user,
            enable_home_css: true,
            javascript_file: "grant_action.js"
        });
    });

    route.post('/users/grant-action', async (req: Request, res: Response) => {
        const uid = req.body.uid;
        const action = req.body.action;

        let actionUrlExtension;
        switch (action) {
            case 'grant_user_role':
                actionUrlExtension = 'grant-role/' + 1;
                break;
            case 'grant_manager_role':
                actionUrlExtension = 'grant-role/' + 2;
                break;
            case 'grant_admin_role':
                actionUrlExtension = 'grant-role/' + 3;
                break;
            case 'grant_access_role':
                actionUrlExtension = 'iq-tests/grant-permission';
                break;
            case 'revoke_user_role':
                actionUrlExtension = 'revoke-role/' + 1;
                break;
            case 'revoke_manager_role':
                actionUrlExtension = 'revoke-role/' + 2;
                break;
            case 'revoke_admin_role':
                actionUrlExtension = 'revoke-role/' + 3;
                break;
            case 'revoke_access_role':
                actionUrlExtension = 'iq-tests/restrict-permission';
                break;
        }

        const url = `${config.adapterIP}api/users/${uid}/${actionUrlExtension}`;
        const settings = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + req["session"]["currentUser"]["token"]
            },
        };
        const response = (await fetch(url, settings));
        if (response.status == 200){
            return res.redirect(`/users/${uid}`);
        }
        return res.sendStatus(response.status);
    });
};
