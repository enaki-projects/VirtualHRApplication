import {Item} from "./item";
import {Maintainer} from "./maintainer";

export interface Cart{
    cartId: number;
    name: string;
    creatorId: number;
    description?: string;
    maintainers: Maintainer[];
    items: Item[];
}
