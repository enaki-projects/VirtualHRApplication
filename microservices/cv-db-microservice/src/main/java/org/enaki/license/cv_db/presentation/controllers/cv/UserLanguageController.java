package org.enaki.license.cv_db.presentation.controllers.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateUserLanguage;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateUserLanguage;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IUserLanguageService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class UserLanguageController {
    final IUserLanguageService _userLanguageService;

    public UserLanguageController(IUserLanguageService userLanguageService) {
        _userLanguageService = userLanguageService;
    }

    @RequestMapping(value = "/user-languages", method = RequestMethod.GET)
    public ResponseEntity<?> getUserLanguages(@RequestParam(required=false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _userLanguageService.getAllUserLanguages(): _userLanguageService.getUserLanguagesByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/user-languages/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserLanguageById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _userLanguageService.getUserLanguageById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/{uid}/user-languages", method = RequestMethod.GET)
    public ResponseEntity<?> getUserUserLanguages(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _userLanguageService.getUserLanguagesByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/user-languages", method=RequestMethod.POST)
    public ResponseEntity<?> createUserLanguage(@PathVariable("uid") Long uid,
                                         @RequestBody BusinessCreateUserLanguage userLanguage,
                                         HttpServletRequest request){
        userLanguage.uid = uid;
        Response<?> response = _userLanguageService.createUserLanguage(userLanguage);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/user-languages/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateUserLanguage(@PathVariable("id") Long id,
                                         @RequestBody BusinessUpdateUserLanguage userLanguage,
                                         HttpServletRequest request){
        Response<?> response = _userLanguageService.updateUserLanguage(id, userLanguage);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/user-languages/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUserLanguage(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _userLanguageService.deleteUserLanguageId(id);
        return ControllerUtils.makeResponse(response, request);
    }
}