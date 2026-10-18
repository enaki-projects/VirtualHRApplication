package org.enaki.license.cv_db.presentation.controllers.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateLocation;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateLocation;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.ILocationService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class LocationController {
    final ILocationService _locationService;

    public LocationController(ILocationService locationService) {
        _locationService = locationService;
    }

    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public ResponseEntity<?> getLocations(@RequestParam(required=false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _locationService.getAllLocations(): _locationService.getLocationsByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/locations/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getLocationById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _locationService.getLocationById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/{uid}/locations", method = RequestMethod.GET)
    public ResponseEntity<?> getUserLocations(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _locationService.getLocationsByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/locations", method=RequestMethod.POST)
    public ResponseEntity<?> createLocation(@PathVariable("uid") Long uid,
                                         @RequestBody BusinessCreateLocation location,
                                         HttpServletRequest request){
        location.uid = uid;
        Response<?> response = _locationService.createLocation(location);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/locations/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateLocation(@PathVariable("id") Long id,
                                         @RequestBody BusinessUpdateLocation location,
                                         HttpServletRequest request){
        Response<?> response = _locationService.updateLocation(id, location);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/locations/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteLocation(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _locationService.deleteLocationId(id);
        return ControllerUtils.makeResponse(response, request);
    }
}