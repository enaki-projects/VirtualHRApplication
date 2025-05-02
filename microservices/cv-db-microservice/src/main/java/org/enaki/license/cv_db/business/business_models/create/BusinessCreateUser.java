package org.enaki.license.cv_db.business.business_models.create;

import org.enaki.license.cv_db.persistence.entities.users.User;

public class BusinessCreateUser {
    public String email;
    public String password;

    public BusinessCreateUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static User toUser(BusinessCreateUser user) {
        return new User(Integer.toUnsignedLong(0), user.email, user.password);
    }

    public boolean isValid() {
        return (email != null) && (password != null);
    }

    public String getRequiredFieldsMessage(){
        return "User require fields (email, password)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("User with value field (email): (%s) is existent", email);
    }
}
