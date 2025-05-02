package org.enaki.license.cv_db.business.business_models.update;

import org.enaki.license.cv_db.persistence.entities.users.User;

public class BusinessUpdateUser {
    public String email;
    public String password;

    public BusinessUpdateUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static User toUser(Long uid, BusinessUpdateUser user) {
        return new User(uid, user.email, user.password);
    }

    public Boolean isValid() {
        return (email != null && password != null);
    }

    public String getRequiredFieldsMessage(){
        return "User require fields (email, password)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("User with value field (email): (%s) is existent", email);
    }
}
