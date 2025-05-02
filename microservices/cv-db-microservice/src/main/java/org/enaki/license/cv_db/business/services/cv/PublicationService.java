package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreatePublication;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessPublication;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdatePublication;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IPublicationService;
import org.enaki.license.cv_db.persistence.entities.cv.Publication;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.IPublicationRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicationService implements IPublicationService {
    private final IPublicationRepository _publicationRepository;
    private final IUserRepository _userRepository;

    public PublicationService(IPublicationRepository publicationRepository, IUserRepository userRepository) {
        _publicationRepository = publicationRepository;
        _userRepository = userRepository;
    }

    @Override
    public Response<List<BusinessPublication>> getAllPublications() {
        try{
            List<Publication> publicationList = _publicationRepository.getAll();
            return new Response<>(true, 200, BusinessPublication.toBusinessPublicationList(publicationList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessPublication>> getPublicationsByUserId(Long uid) {
        try{
            List<Publication> publicationList = _publicationRepository.getAllByUid(uid);
            return new Response<>(true, 200, BusinessPublication.toBusinessPublicationList(publicationList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessPublication>> getPublicationsByName(String name) {
        try{
            List<Publication> publicationList = _publicationRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessPublication.toBusinessPublicationList(publicationList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessPublication> getPublicationById(Long id) {
        try{
            Publication publication = _publicationRepository.get(id);
            if (publication == null){
                return CustomResponse.getItemNotFoundResponse("Publication", id);
            }
            return new Response<>(true, 200, BusinessPublication.toBusinessPublication(publication));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deletePublicationId(Long id) {
        try{
            Boolean success = _publicationRepository.delete(id);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "Publication Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }


    @Override
    public Response<BusinessId> createPublication(BusinessCreatePublication publication) {
        try{
            if (!publication.isValid())
                return new Response<>(false, 400, null, publication.getRequiredFieldsMessage());
            User user = _userRepository.get(publication.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", publication.uid);
            }

            Long id = _publicationRepository.add(BusinessCreatePublication.toPublication(publication));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create publication");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, publication.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updatePublication(Long id, BusinessUpdatePublication publication) {
        try{
            if (!publication.isValid())
                return new Response<>(false, 400, null, publication.getRequiredFieldsMessage());
            User user = _userRepository.get(publication.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", publication.uid);
            }

            Publication foundPublication = _publicationRepository.get(id);
            Publication dbPublication = BusinessUpdatePublication.toPublication(id, publication);
            if (foundPublication == null){
                Boolean success = _publicationRepository.addWithId(dbPublication);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With Publication Update");
            }
            Boolean success = _publicationRepository.update(id, BusinessUpdatePublication.toPublication(id, publication));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With Publication Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, publication.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
