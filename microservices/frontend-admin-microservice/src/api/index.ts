import {Router} from 'express';
import home from './routes/home';
import cv from './routes/cv';
import login from './routes/login';
import logout from './routes/logout';
import users from "./routes/users";

export default () => {
	const app = Router();
	login(app);
	logout(app);
	home(app);
	cv(app);
	users(app);

	return app
}
