import config from "../config";

const moment = require('moment');

export default {
    getAge: function(value){
        const today = moment();
        const birthdate = moment(value);
        return today.diff(birthdate, 'years');
    },
    getUrlPhoto(user){
        if (user.cv.hasImage){
            return `${config.adapterIP}api/users/${user.uid}/image`;
        }
        switch (user.cv.gender.toLowerCase()) {
            case 'male':
                return `/images/male.png`;
            case 'female':
                return  `/images/female.png`;
            default:
                return  `/images/user.png`;
        }
    },
    getRoles(roles){
        return roles.map(role => role.roleName).join(", ")
    }
};
