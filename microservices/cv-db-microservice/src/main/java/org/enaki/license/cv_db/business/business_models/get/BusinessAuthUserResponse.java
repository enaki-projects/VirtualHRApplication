package org.enaki.license.cv_db.business.business_models.get;

import org.enaki.license.cv_db.persistence.entities.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessAuthUserResponse {
    public Long uid;
    public String email;
    public List<String> urlRoles = new ArrayList<>();

    public BusinessAuthUserResponse(Long uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public BusinessAuthUserResponse addRolesIds(List<Long> userRolesIds, String serverAddress) {
        urlRoles = userRolesIds.stream()
                .map(it -> String.format("%s/api/roles/%s", serverAddress, it))
                .collect(Collectors.toList());
        return this;
    }

    public static BusinessAuthUserResponse toBusinessAuthUserResponse(User user) {
        return new BusinessAuthUserResponse(user.uid, user.email);
    }
}
