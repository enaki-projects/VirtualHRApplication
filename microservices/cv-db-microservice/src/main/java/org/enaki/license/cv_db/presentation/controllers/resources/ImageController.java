package org.enaki.license.cv_db.presentation.controllers.resources;

import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.resources.IImageService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api")
public class ImageController {

    private final IImageService _imageService;

    public ImageController(IImageService imageService) {
        this._imageService = imageService;
    }

    @RequestMapping(value="users/{uid}/image", method = RequestMethod.GET)
    public ResponseEntity<?> getImage(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<Resource> response = _imageService.getImage(uid);
        return ResponseEntity.status(response.code)
                .contentType(MediaTypeFactory
                    .getMediaType(response.data)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(response.data);
    }

    @RequestMapping(value="users/{uid}/image", method = RequestMethod.PUT, consumes = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable("uid") Long uid, @RequestBody byte[] bytes, HttpServletRequest request){
        Response<BusinessId> response = _imageService.updateImage(uid, bytes);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="users/{uid}/image", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteImage(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _imageService.deleteImage(uid);
        return ControllerUtils.makeResponse(response, request);
    }
}


