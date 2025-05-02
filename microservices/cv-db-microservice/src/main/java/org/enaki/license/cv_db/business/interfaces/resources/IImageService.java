package org.enaki.license.cv_db.business.interfaces.resources;

import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.springframework.core.io.Resource;

public interface IImageService {
    Response<Resource> getImage(Long uid);

    Response<BusinessId> updateImage(Long uid, byte[] bytes);
    Response<?> deleteImage(Long uid);
}
