package org.enaki.license.cv_db.presentation.controllers.cv;

import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.ICitizenshipService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class CitizenshipController {
    public CitizenshipController(ICitizenshipService _citizenshipService) {
        this._citizenshipService = _citizenshipService;
    }

    final ICitizenshipService _citizenshipService;


    @RequestMapping(value = "/{uid}/citizenship", method = RequestMethod.GET)
    public ResponseEntity<?> getUserNationalitiesById(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _citizenshipService.getUserNationalitiesByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/citizenship/{nationalityId}", method=RequestMethod.POST)
    public ResponseEntity<?> createUserNationality(@PathVariable("uid") Long uid,
                                                @PathVariable("nationalityId") Long nationalityId,
                                         HttpServletRequest request){
        Response<?> response = _citizenshipService.addUserNationality(uid, nationalityId);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/citizenship/{nationalityId}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteUserNationality(@PathVariable("uid") Long uid,
                                                   @PathVariable("nationalityId") Long nationalityId,
                                                   HttpServletRequest request){
        Response<?> response = _citizenshipService.deleteUserNationalityId(uid, nationalityId);
        return ControllerUtils.makeResponse(response, request);
    }
}