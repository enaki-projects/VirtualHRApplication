package org.enaki.license.cv_db.presentation.controllers.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateOrganisation;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateOrganisation;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IOrganisationService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/")
public class OrganisationController {
    final IOrganisationService _organisationService;

    public OrganisationController(IOrganisationService organisationService) {
        _organisationService = organisationService;
    }

    @RequestMapping(value = "/organisations", method = RequestMethod.GET)
    public ResponseEntity<?> getOrganizations(@RequestParam(required=false) String name,
                                              @RequestParam(required=false) Long cityId,
                                              HttpServletRequest request){
        Response<?> response = (name == null) ? _organisationService.getAllOrganisations():
                (cityId == null) ? _organisationService.getOrganisationsByName(name):
                        _organisationService.getOrganisationsByNameAndCityId(name, cityId);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/organisations/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrganizationById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _organisationService.getOrganisationById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/organisations", method=RequestMethod.POST)
    public ResponseEntity<?> createOrganization(@RequestBody BusinessCreateOrganisation organization,
                                         HttpServletRequest request){
        Response<?> response = _organisationService.createOrganisation(organization);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/organisations/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateOrganization(@PathVariable("id") Long id,
                                         @RequestBody BusinessUpdateOrganisation organization,
                                         HttpServletRequest request){
        Response<?> response = _organisationService.updateOrganisation(id, organization);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/organisations/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteOrganisation(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _organisationService.deleteOrganisationId(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/organisations-by-city/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrganisationsByCityId(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response =_organisationService.getOrganisationsByCityId(id);
        return ControllerUtils.makeResponse(response, request);
    }
}