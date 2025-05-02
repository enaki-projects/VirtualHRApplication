package org.enaki.license.cv_db.presentation.controllers.user;

import org.enaki.license.cv_db.business.business_models.get.BusinessAuthUser;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.users.IUserService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value="api/auth")
public class AuthController {
    private final IUserService _userService;

    public AuthController(IUserService userService) {
        _userService = userService;
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<?> loginUser(@RequestBody BusinessAuthUser user, HttpServletRequest request){
        Response<?> response = _userService.login(user);
        return ControllerUtils.makeResponse(response, request);
    }
}

