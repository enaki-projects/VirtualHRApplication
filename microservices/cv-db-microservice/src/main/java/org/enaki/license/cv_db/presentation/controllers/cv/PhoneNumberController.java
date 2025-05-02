package org.enaki.license.cv_db.presentation.controllers.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreatePhoneNumber;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdatePhoneNumber;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IPhoneNumberService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class PhoneNumberController {
    final IPhoneNumberService _phoneNumberService;

    public PhoneNumberController(IPhoneNumberService phoneNumberService) {
        _phoneNumberService = phoneNumberService;
    }

    @RequestMapping(value = "/phone-numbers", method = RequestMethod.GET)
    public ResponseEntity<?> getPhoneNumbers(@RequestParam(required=false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _phoneNumberService.getAllPhoneNumbers(): _phoneNumberService.getPhoneNumbersByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/phone-numbers/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPhoneNumberById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _phoneNumberService.getPhoneNumberById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/{uid}/phone-numbers", method = RequestMethod.GET)
    public ResponseEntity<?> getUserPhoneNumbers(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _phoneNumberService.getPhoneNumbersByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/phone-numbers", method=RequestMethod.POST)
    public ResponseEntity<?> createPhoneNumber(@PathVariable("uid") Long uid,
                                         @RequestBody BusinessCreatePhoneNumber phoneNumber,
                                         HttpServletRequest request){
        phoneNumber.uid = uid;
        Response<?> response = _phoneNumberService.createPhoneNumber(phoneNumber);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/phone-numbers/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updatePhoneNumber(@PathVariable("id") Long id,
                                         @RequestBody BusinessUpdatePhoneNumber phoneNumber,
                                         HttpServletRequest request){
        Response<?> response = _phoneNumberService.updatePhoneNumber(id, phoneNumber);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/phone-numbers/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePhoneNumber(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _phoneNumberService.deletePhoneNumberId(id);
        return ControllerUtils.makeResponse(response, request);
    }
}