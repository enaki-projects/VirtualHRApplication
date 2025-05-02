import {environment as env} from "./environment";
import {environment as envProd} from "./environment.prod";

export const config = {
    environment: (process.env.NODE_ENV === 'production')? envProd: env
}

