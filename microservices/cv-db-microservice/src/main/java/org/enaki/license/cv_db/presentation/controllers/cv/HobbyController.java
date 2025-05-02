package org.enaki.license.cv_db.presentation.controllers.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateHobby;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateHobby;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IHobbyService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class HobbyController {
    final IHobbyService _hobbyService;

    public HobbyController(IHobbyService hobbyService) {
        _hobbyService = hobbyService;
    }

    @RequestMapping(value = "/hobbies", method = RequestMethod.GET)
    public ResponseEntity<?> getHobbies(@RequestParam(required=false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _hobbyService.getAllHobbies(): _hobbyService.getHobbiesByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/hobbies/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getHobbyById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _hobbyService.getHobbyById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/{uid}/hobbies", method = RequestMethod.GET)
    public ResponseEntity<?> getUserHobbies(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _hobbyService.getHobbiesByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/hobbies", method=RequestMethod.POST)
    public ResponseEntity<?> createHobby(@PathVariable("uid") Long uid,
                                         @RequestBody BusinessCreateHobby hobby,
                                         HttpServletRequest request){
        hobby.uid = uid;
        Response<?> response = _hobbyService.createHobby(hobby);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/hobbies/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateHobby(@PathVariable("id") Long id,
                                         @RequestBody BusinessUpdateHobby hobby,
                                         HttpServletRequest request){
        Response<?> response = _hobbyService.updateHobby(id, hobby);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/hobbies/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteHobby(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _hobbyService.deleteHobbyId(id);
        return ControllerUtils.makeResponse(response, request);
    }
}