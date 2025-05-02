package org.enaki.license.cv_db.presentation.controllers.user;

import org.enaki.license.cv_db.business.business_models.create.BusinessCreateUser;
import org.enaki.license.cv_db.business.business_models.update.BusinessUpdatePassword;
import org.enaki.license.cv_db.business.business_models.update.BusinessUpdateUser;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.users.IUserService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value="api/users")
public class UserController {
    private final IUserService _userService;

    public UserController(IUserService userService) {
        _userService = userService;
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String email, HttpServletRequest request){
        Response<?> response = (email == null) ? _userService.getAllUsers(): _userService.getUserByEmail(email);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _userService.getUserById(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _userService.deleteUserById(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody BusinessCreateUser user, HttpServletRequest request){
        Response<?> response = _userService.createUser(user);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("uid") Long uid, @RequestBody BusinessUpdateUser user, HttpServletRequest request){
        Response<?> response = _userService.updateUser(uid, user);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/grant-role/{roleId}", method=RequestMethod.POST)
    public ResponseEntity<?> grantRole(@PathVariable("uid") Long uid,
                                        @PathVariable("roleId") Long roleId,
                                        HttpServletRequest request){
        Response<?> response = _userService.grantRole(uid, roleId);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/revoke-role/{roleId}", method=RequestMethod.POST)
    public ResponseEntity<?> revokeRole(@PathVariable("uid") Long uid,
                                        @PathVariable("roleId") Long roleId,
                                        HttpServletRequest request){
        Response<?> response = _userService.revokeRole(uid, roleId);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/change-password", method=RequestMethod.PUT)
    public ResponseEntity<?> updatePassword(@PathVariable("uid") Long uid,
                                            @RequestBody BusinessUpdatePassword userPassword, HttpServletRequest request){
        Response<?> response = _userService.changePassword(uid, userPassword);
        return ControllerUtils.makeResponse(response, request);
    }
}
