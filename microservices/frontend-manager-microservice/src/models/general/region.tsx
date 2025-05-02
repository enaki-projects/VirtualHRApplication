import {Country} from './country';

export interface Region {
    regionId: number;
    regionName: string;
    regionCode: string;
    country: Country;
}
