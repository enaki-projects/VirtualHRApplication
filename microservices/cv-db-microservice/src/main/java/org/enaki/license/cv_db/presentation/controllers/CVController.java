package org.enaki.license.cv_db.presentation.controllers;

import org.enaki.license.cv_db.business.business_models.create.BusinessCreateCV;
import org.enaki.license.cv_db.business.business_models.update.BusinessUpdateCV;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.ICVService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cvs")
public class CVController {
    private final ICVService _cvService;

    public CVController(ICVService cvService) {
        _cvService = cvService;
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<?> getCVs(@RequestParam(required = false) String email , HttpServletRequest request){
        Response<?> response = (email == null) ? _cvService.getAllCVs(): _cvService.getCVByEmail(email);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCVById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response =_cvService.getCVByUserId(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public ResponseEntity<?> createCV(@RequestBody BusinessCreateCV cv, HttpServletRequest request){
        Response<?> response = _cvService.createCV(cv);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}", method=RequestMethod.PUT)
    public ResponseEntity<?> replaceCV(@PathVariable("uid") Long uid, @RequestBody BusinessUpdateCV cv, HttpServletRequest request){
        Response<?> response = _cvService.updateCV(uid, cv);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteCV(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _cvService.deleteCV(uid);
        return ControllerUtils.makeResponse(response, request);
    }
}
