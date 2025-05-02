package org.enaki.license.cv_db.presentation.controllers.iq;

import org.enaki.license.cv_db.business.business_models.create.iq.BusinessCreateIQTest;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.iq.IIQTestService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/")
public class IQController {
    private final IIQTestService _iqTestService;

    public IQController(IIQTestService iqTestService) {
        _iqTestService = iqTestService;
    }

    @RequestMapping(value="iq-tests", method = RequestMethod.GET)
    public ResponseEntity<?> getIQTests(@RequestParam(value = "basicInfo", required = false, defaultValue = "1") Boolean basicInfo,
                                        HttpServletRequest request){
        Response<?> response = _iqTestService.getAllIQTests(basicInfo);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="iq-tests/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getIQTestById(@PathVariable("id") Long id,
                                           @RequestParam(value = "basicInfo", required = false, defaultValue = "1") Boolean basicInfo,
                                           HttpServletRequest request){
        Response<?> response = _iqTestService.getIQTestById(id, basicInfo);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="iq-tests", method=RequestMethod.POST)
    public ResponseEntity<?> addIQTest(@RequestBody BusinessCreateIQTest iqTest, HttpServletRequest request){
        Response<?> response = _iqTestService.addIQTest(iqTest);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="iq-tests/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteIQTest(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _iqTestService.deleteIQTestById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="users/{uid}/iq-tests", method=RequestMethod.GET)
    public ResponseEntity<?> getIQTestByUid(@PathVariable("uid") Long uid,
                                            @RequestParam(value = "latest", required = false, defaultValue = "0") Boolean latestTest,
                                            @RequestParam(value = "basicInfo", required = false, defaultValue = "1") Boolean basicInfo,
                                            HttpServletRequest request){
        Response<?> response = (latestTest)? _iqTestService.getLatestIQTestByUid(uid, basicInfo):
                _iqTestService.getIQTestsByUid(uid, basicInfo);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="users/{uid}/iq-tests/grant-permission", method=RequestMethod.POST)
    public ResponseEntity<?> addPermissionToRetakeTheTest(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _iqTestService.addPermissionToRetakeTheTest(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="users/{uid}/iq-tests/request-permission", method=RequestMethod.POST)
    public ResponseEntity<?> requestPermissionToRetakeTheTest(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _iqTestService.requestPermissionToRetakeTheTest(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="users/{uid}/iq-tests/restrict-permission", method=RequestMethod.POST)
    public ResponseEntity<?> restrictPermissionTotakeTheTest(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _iqTestService.restrictPermissionToTakeTheTest(uid);
        return ControllerUtils.makeResponse(response, request);
    }
}
