package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateEducationalExperience;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessEducationalExperience;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateEducationalExperience;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IEducationalExperienceService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.cv.EducationalExperience;
import org.enaki.license.cv_db.persistence.entities.geography.EducationalInstitution;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.IEducationalExperienceRepository;
import org.enaki.license.cv_db.persistence.interfaces.geography.IEducationalInstitutionRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationalExperienceService implements IEducationalExperienceService {
    private final IEducationalExperienceRepository _educationalExperienceRepository;
    private final IUserRepository _userRepository;
    private final IEducationalInstitutionRepository _educationalInstitutionRepository;
    private final ServerEnvUtils _serverEnvUtils;

    public EducationalExperienceService(IEducationalExperienceRepository educationalExperienceRepository, IUserRepository userRepository, IEducationalInstitutionRepository educationalInstitutionRepository, ServerEnvUtils serverEnvUtils) {
        _educationalExperienceRepository = educationalExperienceRepository;
        _userRepository = userRepository;
        _educationalInstitutionRepository = educationalInstitutionRepository;
        _serverEnvUtils = serverEnvUtils;
    }

    @Override
    public Response<List<BusinessEducationalExperience>> getAllEducationalExperiences() {
        try{
            List<EducationalExperience> educationalExperienceList = _educationalExperienceRepository.getAll();
            return new Response<>(true, 200, BusinessEducationalExperience.toBusinessEducationalExperienceList(educationalExperienceList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessEducationalExperience>> getEducationalExperiencesByUserId(Long uid) {
        try{
            List<EducationalExperience> educationalExperienceList = _educationalExperienceRepository.getAllByUid(uid);
            return new Response<>(true, 200, BusinessEducationalExperience.toBusinessEducationalExperienceList(educationalExperienceList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessEducationalExperience>> getEducationalExperiencesByName(String name) {
        try{
            List<EducationalExperience> educationalExperienceList = _educationalExperienceRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessEducationalExperience.toBusinessEducationalExperienceList(educationalExperienceList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessEducationalExperience> getEducationalExperienceById(Long id) {
        try{
            EducationalExperience educationalExperience = _educationalExperienceRepository.get(id);
            if (educationalExperience == null){
                return CustomResponse.getItemNotFoundResponse("Educational Experience", id);
            }
            return new Response<>(true, 200, BusinessEducationalExperience.toBusinessEducationalExperience(educationalExperience, _serverEnvUtils.getServerUrlPrefix()));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteEducationalExperienceId(Long id) {
        try{
            Boolean success = _educationalExperienceRepository.delete(id);
            if (!success){
                return CustomResponse.getItemNotFoundResponse("Educational Experience", id);
            }
            return new Response<>(true, 204, null,  "");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }


    @Override
    public Response<BusinessId> createEducationalExperience(BusinessCreateEducationalExperience educationalExperience) {
        try{
            if (!educationalExperience.isValid())
                return new Response<>(false, 400, null, educationalExperience.getRequiredFieldsMessage());
            User user = _userRepository.get(educationalExperience.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", educationalExperience.uid);
            }
            EducationalInstitution educationalInstitution = _educationalInstitutionRepository.get(educationalExperience.educationalInstitutionId);
            if (educationalInstitution == null){
                return CustomResponse.getItemNotFoundResponse("Educational Institution", educationalExperience.educationalInstitutionId);
            }

            Long id = _educationalExperienceRepository.add(BusinessCreateEducationalExperience.toEducationalExperience(educationalExperience));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create educationalExperience");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return CustomResponse.getItemIsExistentResponse(educationalExperience.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updateEducationalExperience(Long id, BusinessUpdateEducationalExperience educationalExperience) {
        try{
            if (!educationalExperience.isValid())
                return new Response<>(false, 400, null, educationalExperience.getRequiredFieldsMessage());
            User user = _userRepository.get(educationalExperience.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", educationalExperience.uid);
            }
            EducationalInstitution educationalInstitution = _educationalInstitutionRepository.get(educationalExperience.educationalInstitutionId);
            if (educationalInstitution == null){
                return CustomResponse.getItemNotFoundResponse("Educational Institution", educationalExperience.educationalInstitutionId);
            }

            EducationalExperience foundEducationalExperience = _educationalExperienceRepository.get(id);
            EducationalExperience dbEducationalExperience = BusinessUpdateEducationalExperience.toEducationalExperience(id, educationalExperience);
            if (foundEducationalExperience == null){
                Boolean success = _educationalExperienceRepository.addWithId(dbEducationalExperience);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With EducationalExperience Update");
            }
            Boolean success = _educationalExperienceRepository.update(id, BusinessUpdateEducationalExperience.toEducationalExperience(id, educationalExperience));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With EducationalExperience Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){

            return CustomResponse.getItemIsExistentResponse(educationalExperience.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
