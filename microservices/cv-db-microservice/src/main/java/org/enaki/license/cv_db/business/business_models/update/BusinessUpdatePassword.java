package org.enaki.license.cv_db.business.business_models.update;


public class BusinessUpdatePassword {
    public String password;
    public String oldPassword;

    public BusinessUpdatePassword(String password, String oldPassword) {
        this.password = password;
        this.oldPassword = oldPassword;
    }

    public Boolean isValid() {
        return (password != null && oldPassword != null);
    }

    public String getRequiredFieldsMessage(){
        return "User Password Change require fields (password, oldPassword)";
    }
}
