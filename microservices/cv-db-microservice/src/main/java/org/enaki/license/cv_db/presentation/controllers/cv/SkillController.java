package org.enaki.license.cv_db.presentation.controllers.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateSkill;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateSkill;
import org.enaki.license.cv_db.business.companion.SkillType;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.ISkillService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class SkillController {
    final ISkillService _skillService;

    public SkillController(ISkillService skillService) {
        _skillService = skillService;
    }

    @RequestMapping(value = "/skills/{skillType}", method = RequestMethod.GET)
    public ResponseEntity<?> getSkills(@PathVariable("skillType") String skillType, @RequestParam(required=false) String name, HttpServletRequest request){
        if (!isValidSkillService(skillType))
            return ControllerUtils.makeResponse(getInvalidSkillResponse(), request);

        Response<?> response = (name == null) ? _skillService.getAllSkills(): _skillService.getSkillsByName(name);
        return ControllerUtils.makeResponse(response, request);
    }



    @RequestMapping(value = "/skills/{skillType}/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSkillById(@PathVariable("skillType") String skillType, @PathVariable("id") Long id, HttpServletRequest request){
        if (!isValidSkillService(skillType))
            return ControllerUtils.makeResponse(getInvalidSkillResponse(), request);

        Response<?> response = _skillService.getSkillById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/skill-by-name/{skillType}/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> getSkillById(@PathVariable("skillType") String skillType, @PathVariable("name") String name, HttpServletRequest request){
        if (!isValidSkillService(skillType))
            return ControllerUtils.makeResponse(getInvalidSkillResponse(), request);

        Response<?> response = _skillService.getSkillByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/{uid}/skills/{skillType}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserSkills(@PathVariable("skillType") String skillType, @PathVariable("uid") Long uid, HttpServletRequest request){
        if (!isValidSkillService(skillType))
            return ControllerUtils.makeResponse(getInvalidSkillResponse(), request);

        Response<?> response = _skillService.getSkillsByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/skills/{skillType}", method=RequestMethod.POST)
    public ResponseEntity<?> createSkill(@PathVariable("skillType") String skillType, @RequestBody BusinessCreateSkill skill,
                                         HttpServletRequest request){
        if (!isValidSkillService(skillType))
            return ControllerUtils.makeResponse(getInvalidSkillResponse(), request);

        Response<?> response = _skillService.createSkill(skill);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/skills/{skillType}/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateSkill(@PathVariable("skillType") String skillType, @PathVariable("id") Long id,
                                         @RequestBody BusinessUpdateSkill skill,
                                         HttpServletRequest request){
        if (!isValidSkillService(skillType))
            return ControllerUtils.makeResponse(getInvalidSkillResponse(), request);

        Response<?> response = _skillService.updateSkill(id, skill);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/skills/{skillType}/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSkill(@PathVariable("skillType") String skillType, @PathVariable("id") Long id, HttpServletRequest request){
        if (!isValidSkillService(skillType))
            return ControllerUtils.makeResponse(getInvalidSkillResponse(), request);

        Response<?> response = _skillService.deleteSkillId(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/{skillType}", method = RequestMethod.POST)
    public ResponseEntity<?> addUserSkill(@PathVariable("skillType") String skillType,
                                         @PathVariable("uid") Long uid,
                                         @RequestParam(value = "name") String skillName,
                                         HttpServletRequest request){
        if (!isValidSkillService(skillType))
            return ControllerUtils.makeResponse(getInvalidSkillResponse(), request);

        Response<?> response = _skillService.addUserSkill(uid, skillName);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/{skillType}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUserSkill(@PathVariable("skillType") String skillType,
                                          @PathVariable("uid") Long uid,
                                          @RequestParam(value = "name") String skillName,
                                          HttpServletRequest request){
        if (!isValidSkillService(skillType))
            return ControllerUtils.makeResponse(getInvalidSkillResponse(), request);

        Response<?> response = _skillService.deleteUserSkill(uid, skillName);
        return ControllerUtils.makeResponse(response, request);
    }

    public Boolean isValidSkillService(String skillType){
        switch (skillType){
            case "leadership":
                _skillService.setSkillType(SkillType.LEADERSHIP);
                return true;
            case "organisational":
                _skillService.setSkillType(SkillType.ORGANISATIONAL);
                return true;
            case "digital":
                _skillService.setSkillType(SkillType.DIGITAL);
                return true;
            case "interpersonal":
                _skillService.setSkillType(SkillType.INTERPERSONAL);
                return true;
            default:
                return false;
        }
    }

    public Response<?> getInvalidSkillResponse(){
        return new Response<>(false, 400, null, "Invalid Skill Name.", "Choose between organisational, leadership, digital or interpersonal");
    }
}