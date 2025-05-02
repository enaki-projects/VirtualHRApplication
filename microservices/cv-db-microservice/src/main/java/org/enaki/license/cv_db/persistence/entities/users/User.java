package org.enaki.license.cv_db.persistence.entities.users;

public final class User  {
    public Long uid;
    public String email;
    public String password;

    public User(Long uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.uid = Integer.toUnsignedLong(-1);
        this.email = email;
        this.password = password;
    }
}
