package org.enaki.license.cv_db.presentation.controllers.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreatePublication;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdatePublication;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IPublicationService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class PublicationController {
    final IPublicationService _publicationService;

    public PublicationController(IPublicationService publicationService) {
        _publicationService = publicationService;
    }

    @RequestMapping(value = "/publications", method = RequestMethod.GET)
    public ResponseEntity<?> getPublications(@RequestParam(required=false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _publicationService.getAllPublications(): _publicationService.getPublicationsByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/publications/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPublicationById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _publicationService.getPublicationById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/{uid}/publications", method = RequestMethod.GET)
    public ResponseEntity<?> getUserPublications(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _publicationService.getPublicationsByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/publications", method=RequestMethod.POST)
    public ResponseEntity<?> createPublication(@PathVariable("uid") Long uid,
                                         @RequestBody BusinessCreatePublication publication,
                                         HttpServletRequest request){
        publication.uid = uid;
        Response<?> response = _publicationService.createPublication(publication);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/publications/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updatePublication(@PathVariable("id") Long id,
                                         @RequestBody BusinessUpdatePublication publication,
                                         HttpServletRequest request){
        Response<?> response = _publicationService.updatePublication(id, publication);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/publications/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePublication(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _publicationService.deletePublicationId(id);
        return ControllerUtils.makeResponse(response, request);
    }
}