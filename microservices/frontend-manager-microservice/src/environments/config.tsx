import {environment as env} from "./environment";
import {environment as envProd} from "./environment.prod";

export const config = {
    environment: import.meta.env.PROD ? envProd : env
}
