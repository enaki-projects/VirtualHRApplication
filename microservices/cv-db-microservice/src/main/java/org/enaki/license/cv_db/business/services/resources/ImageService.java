package org.enaki.license.cv_db.business.services.resources;

import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.resources.IImageService;
import org.enaki.license.cv_db.persistence.entities.CV;
import org.enaki.license.cv_db.persistence.interfaces.cv.ICVRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Service
public class ImageService implements IImageService {
    private final Logger _logger = LoggerFactory.getLogger(ImageService.class);

    public ImageService(ICVRepository _cvRepository, ResourceLoader resourceLoader) {
        this._cvRepository = _cvRepository;
        this._resourceLoader = resourceLoader;
    }

    private final ICVRepository _cvRepository;
    private final ResourceLoader _resourceLoader;

    public Response<Resource> getImage(Long uid){
        CV cv = _cvRepository.get(uid);
        if (cv == null)
            return new Response(false, 404, null, "User Not Found");

        String path = String.format("file:src/main/resources/images/%s/%s", cv.uid, cv.imageName);
        Resource resource = _resourceLoader.getResource(path);
        if (resource.exists())
            return new Response(true, 200, resource);
        return new Response(false, 404, null, "Image Not Found");
    }

    @Override
    public Response<BusinessId> updateImage(Long uid, byte[] bytes) {
        CV cv = _cvRepository.get(uid);
        if (cv == null)
            return new Response(false, 404, null, "User Not Found");
        if (cv.imageName != null)
            if (!this.deleteImageFile(uid, cv.imageName))
                return new Response(false, 500, null, "Cannot Delete existent image");
        String extension = "png";
        String imageName = String.format("%s.%s", uid, extension);
        if (!createImageFile(bytes, uid, "png"))
            return new Response(false, 500, null, "Cannot Create A New Image");
        if (!_cvRepository.update_photo(uid, imageName))
            return new Response(false, 500, "Cannot Update User CV in Database");
        return new Response(true, 200, null);
    }

    @Override
    public Response<?> deleteImage(Long uid) {
        CV cv = _cvRepository.get(uid);
        if (cv == null)
            return new Response(false, 404, null, "User Not Found");
        if (cv.imageName == null)
            return new Response(false, 404, null, "User Does Not Have An Image");

        String path = String.format("file:src/main/resources/images/%s/%s", cv.uid, cv.imageName);
        Resource resource = _resourceLoader.getResource(path);
        if (!resource.exists())
            return new Response(false, 404, null, "User Has An Invalid Image");

        if (!_cvRepository.delete_photo(uid))
                return new Response(false, 500, "Cannot Update User CV in Database");

        if (this.deleteImageFile(uid, cv.imageName)) {
            return new Response(true, 200, null);
        } else {
            return new Response(false, 500, null);
        }
    }

    private boolean deleteImageFile(Long uid, String imageName) {
        String path = String.format("src/main/resources/images/%s/%s", uid, imageName);
        try {
            File file = new File(path);
            return file.delete();
        }
        catch(Exception e)
        {
            return false;
        }
    }

    private boolean createImageFile(byte[] bytes, Long uid, String extension){
        String path = String.format("src/main/resources/images/%s/%s.%s", uid, uid, extension);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            BufferedImage bImage = ImageIO.read(bis);
            File outputFile = new File(path);
            outputFile.getParentFile().mkdirs();
            ImageIO.write(bImage, extension, outputFile );
            _logger.info(String.format("Image updated successfully for user with uid %s", uid));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
