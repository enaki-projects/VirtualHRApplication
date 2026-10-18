package org.enaki.license.cv_db.presentation.controllers.world;

import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.world.IEducationalService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/institutions")
public class InstitutionController {
    final IEducationalService _educationalService;

    public InstitutionController(IEducationalService educationalService) {
        _educationalService = educationalService;
    }

    @RequestMapping(value="/educational", method = RequestMethod.GET)
    public ResponseEntity<?> getEducationalInstitutions(@RequestParam(required = false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _educationalService.getAllEducationalInstitutions():
                _educationalService.getEducationalInstitutionsByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/educational-basic", method = RequestMethod.GET)
    public ResponseEntity<?> getEducationalInstitutionsBasicInfo(@RequestParam(required = false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _educationalService.getAllEducationalInstitutionsBasicInformation():
                _educationalService.getEducationalInstitutionsBasicInformationByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/educational/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getEducationalInstitutions(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response =_educationalService.getEducationalInstitution(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/educational-by-country/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getEducationalInstitutionByCountryId(@PathVariable("id") Long id, @RequestParam(required = false, defaultValue = "true") Boolean basic, HttpServletRequest request){
        Response<?> response =_educationalService.getEducationalInstitutionsByCountryId(id, basic);
        return ControllerUtils.makeResponse(response, request);
    }
}
