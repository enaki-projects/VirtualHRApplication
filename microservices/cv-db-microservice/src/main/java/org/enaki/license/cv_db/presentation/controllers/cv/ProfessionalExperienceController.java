package org.enaki.license.cv_db.presentation.controllers.cv;


import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateProfessionalExperience;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateProfessionalExperience;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IProfessionalExperienceService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class ProfessionalExperienceController {
    final IProfessionalExperienceService _professionalExperienceService;

    public ProfessionalExperienceController(IProfessionalExperienceService professionalExperienceService) {
        _professionalExperienceService = professionalExperienceService;
    }

    @RequestMapping(value = "/professional-experiences", method = RequestMethod.GET)
    public ResponseEntity<?> getProfessionalExperiences(@RequestParam(required=false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _professionalExperienceService.getAllProfessionalExperiences(): _professionalExperienceService.getProfessionalExperiencesByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/professional-experiences/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProfessionalExperienceById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _professionalExperienceService.getProfessionalExperienceById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/{uid}/professional-experiences", method = RequestMethod.GET)
    public ResponseEntity<?> getUserProfessionalExperiences(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _professionalExperienceService.getProfessionalExperiencesByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/professional-experiences", method=RequestMethod.POST)
    public ResponseEntity<?> createProfessionalExperience(@PathVariable("uid") Long uid,
                                         @RequestBody BusinessCreateProfessionalExperience professionalExperience,
                                         HttpServletRequest request){
        professionalExperience.uid = uid;
        Response<?> response = _professionalExperienceService.createProfessionalExperience(professionalExperience);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/professional-experiences/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateProfessionalExperience(@PathVariable("id") Long id,
                                         @RequestBody BusinessUpdateProfessionalExperience professionalExperience,
                                         HttpServletRequest request){
        Response<?> response = _professionalExperienceService.updateProfessionalExperience(id, professionalExperience);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/professional-experiences/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProfessionalExperience(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _professionalExperienceService.deleteProfessionalExperienceId(id);
        return ControllerUtils.makeResponse(response, request);
    }
}

