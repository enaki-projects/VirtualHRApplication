package org.enaki.license.cv_db.business.business_models.get;

import org.enaki.license.cv_db.persistence.entities.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessUser {
    public Long uid;
    public String email;
    public String password;
    public List<String> roles = new ArrayList<>();

    public BusinessUser(Long uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password = password;
    }

    public static BusinessUser toBusinessUser(User user) {
        return new BusinessUser(user.uid, user.email, user.password);
    }

    public static List<BusinessUser> toBusinessUserList(List<User> userList) {
        return userList.stream()
                .map(BusinessUser::toBusinessUser)
                .collect(Collectors.toList());
    }

    public void addRolesIds(List<Long> userRolesIds, String serverAddress) {
        roles = userRolesIds.stream()
                .map(it -> String.format("%s/api/roles/%s", serverAddress, it))
                .collect(Collectors.toList());
    }
}
