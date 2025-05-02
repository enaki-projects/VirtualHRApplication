package org.enaki.license.cv_db.business.business_models.get;


public class BusinessAuthUser {
    public String email;
    public String password;

    public BusinessAuthUser(String email, String password) {
        this.email = email;
        this.password = password;
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
