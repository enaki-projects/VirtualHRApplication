import config from "../config";

import moment from 'moment';

export default {
    getAge: function(value){
        const today = moment();
        const birthdate = moment(value);
        return today.diff(birthdate, 'years');
    },
    getUrlPhoto(user){
        if (user.cv.hasImage){
            return `${config.publicAdapterUrl}api/users/${user.uid}/image`;
        }
        switch (user.cv.gender.toLowerCase()) {
            case 'male':
                return `${config.basePath}/images/male.png`;
            case 'female':
                return  `${config.basePath}/images/female.png`;
            default:
                return  `${config.basePath}/images/user.png`;
        }
    },
    getRoles(roles){
        return roles.map(role => role.roleName).join(", ")
    }
};
