package org.enaki.license.cv_db.presentation.controllers.world;

import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.world.IGeographyService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/geography")
public class GeographyController {
    final IGeographyService _geographyService;

    public GeographyController(IGeographyService _geographyService) {
        this._geographyService = _geographyService;
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public ResponseEntity<?> getCountries(@RequestParam(required = false) String name,
                                          @RequestParam(required = false, defaultValue = "false") Boolean whichHaveRegions,
                                          @RequestParam(required = false, defaultValue = "false") Boolean whichHaveCities,
                                          @RequestParam(required = false, defaultValue = "false") Boolean whichHaveUniversities,
                                          HttpServletRequest request){
        if (name != null){
            Response<?> response = _geographyService.getCountriesByName(name);
            return ControllerUtils.makeResponse(response, request);

        } else if (whichHaveCities){
            Response<?> response = _geographyService.getAllCountriesWhichHaveCities();
            return ControllerUtils.makeResponse(response, request);

        } else if (whichHaveRegions){
            Response<?> response = _geographyService.getAllCountriesWhichHaveRegions();
            return ControllerUtils.makeResponse(response, request);
        } else if (whichHaveUniversities){
            Response<?> response = _geographyService.getAllCountriesWhichHaveUniversities();
            return ControllerUtils.makeResponse(response, request);
        } else {
            Response<?> response = _geographyService.getAllCountries();
            return ControllerUtils.makeResponse(response, request);
        }
    }

    @RequestMapping(value="/countries/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCountry(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response =_geographyService.getCountry(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/countries/{id}/cities", method = RequestMethod.GET)
    public ResponseEntity<?> getCitiesByCountry(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response =_geographyService.getCitiesByCountryId(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/countries/{id}/regions", method = RequestMethod.GET)
    public ResponseEntity<?> getRegionsByCountry(@PathVariable("id") Long id,
                                                 @RequestParam(required = false, defaultValue = "false") Boolean whichHaveCities,
                                                 HttpServletRequest request){
        if (whichHaveCities){
            Response<?> response = _geographyService.getRegionsWhichHaveCitiesByCountryId(id);
            return ControllerUtils.makeResponse(response, request);
        }
        Response<?> response =_geographyService.getRegionsByCountryId(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public ResponseEntity<?> getCities(@RequestParam(required = false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _geographyService.getAllCities(): _geographyService.getCitiesByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/cities/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCity(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response =_geographyService.getCity(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/regions", method = RequestMethod.GET)
    public ResponseEntity<?> getRegions(@RequestParam(required = false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _geographyService.getAllRegions(): _geographyService.getRegionsByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/regions/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getRegion(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response =_geographyService.getRegion(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/regions/{id}/cities", method = RequestMethod.GET)
    public ResponseEntity<?> getCitiesByRegion(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response =_geographyService.getCitiesByRegionId(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/nationalities", method = RequestMethod.GET)
    public ResponseEntity<?> getNationalities(@RequestParam(required = false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _geographyService.getAllNationalities(): _geographyService.getNationalitiesByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/nationalities/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getNationality(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response =_geographyService.getNationality(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/languages", method = RequestMethod.GET)
    public ResponseEntity<?> getLanguages(@RequestParam(required = false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _geographyService.getAllLanguages(): _geographyService.getLanguagesByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/languages/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getLanguage(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response =_geographyService.getLanguage(id);
        return ControllerUtils.makeResponse(response, request);
    }
}
