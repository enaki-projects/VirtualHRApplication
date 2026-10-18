package org.enaki.license.cv_db.presentation.controllers.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateAward;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateAward;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IAwardService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class AwardController {
    final IAwardService _awardService;

    public AwardController(IAwardService awardService) {
        _awardService = awardService;
    }

    @RequestMapping(value = "/awards", method = RequestMethod.GET)
    public ResponseEntity<?> getAwards(@RequestParam(required=false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _awardService.getAllAwards(): _awardService.getAwardsByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/awards/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAwardById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _awardService.getAwardById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/{uid}/awards", method = RequestMethod.GET)
    public ResponseEntity<?> getUserAwards(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _awardService.getAwardsByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/awards", method=RequestMethod.POST)
    public ResponseEntity<?> createAward(@PathVariable("uid") Long uid,
                                         @RequestBody BusinessCreateAward award,
                                         HttpServletRequest request){
        award.uid = uid;
        Response<?> response = _awardService.createAward(award);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/awards/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateAward(@PathVariable("id") Long id,
                                         @RequestBody BusinessUpdateAward award,
                                         HttpServletRequest request){
        Response<?> response = _awardService.updateAward(id, award);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/awards/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAward(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _awardService.deleteAwardId(id);
        return ControllerUtils.makeResponse(response, request);
    }
}
