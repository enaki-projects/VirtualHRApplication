package org.enaki.license.cv_db.presentation.controllers.cv;


import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateEducationalExperience;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateEducationalExperience;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IEducationalExperienceService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class EducationalExperienceController {
    final IEducationalExperienceService _educationalExperienceService;

    public EducationalExperienceController(IEducationalExperienceService educationalExperienceService) {
        _educationalExperienceService = educationalExperienceService;
    }

    @RequestMapping(value = "/educational-experiences", method = RequestMethod.GET)
    public ResponseEntity<?> getEducationalExperiences(@RequestParam(required=false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _educationalExperienceService.getAllEducationalExperiences(): _educationalExperienceService.getEducationalExperiencesByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/educational-experiences/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getEducationalExperienceById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _educationalExperienceService.getEducationalExperienceById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/{uid}/educational-experiences", method = RequestMethod.GET)
    public ResponseEntity<?> getUserEducationalExperiences(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _educationalExperienceService.getEducationalExperiencesByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/educational-experiences", method=RequestMethod.POST)
    public ResponseEntity<?> createEducationalExperience(@PathVariable("uid") Long uid,
                                         @RequestBody BusinessCreateEducationalExperience educationalExperience,
                                         HttpServletRequest request){
        educationalExperience.uid = uid;
        Response<?> response = _educationalExperienceService.createEducationalExperience(educationalExperience);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/educational-experiences/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateEducationalExperience(@PathVariable("id") Long id,
                                         @RequestBody BusinessUpdateEducationalExperience educationalExperience,
                                         HttpServletRequest request){
        Response<?> response = _educationalExperienceService.updateEducationalExperience(id, educationalExperience);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/educational-experiences/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEducationalExperience(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _educationalExperienceService.deleteEducationalExperienceId(id);
        return ControllerUtils.makeResponse(response, request);
    }
}

