import moment from "moment";
import {Hobby} from "../models/cv/references/hobby";


function getAge(value: Date): number {
    const today = moment();
    const birthdate = moment(value);
    return today.diff(birthdate, 'years');
}

function getHobbiesUnited(hobbies: Hobby[]): string{
    return hobbies.map((hobby) => hobby.name).join(', ');
}

export const userService: any = {
    getAge,
    getHobbiesUnited
};
