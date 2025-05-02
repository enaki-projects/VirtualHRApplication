package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateProfessionalExperience;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessProfessionalExperience;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateProfessionalExperience;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IProfessionalExperienceService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.cv.Organisation;
import org.enaki.license.cv_db.persistence.entities.cv.ProfessionalExperience;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.IOrganisationRepository;
import org.enaki.license.cv_db.persistence.interfaces.cv.IProfessionalExperienceRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessionalExperienceService implements IProfessionalExperienceService {
    private final IProfessionalExperienceRepository _professionalExperienceRepository;
    private final IUserRepository _userRepository;
    private final IOrganisationRepository _organizationRepository;
    private final ServerEnvUtils _serverEnvUtils;

    public ProfessionalExperienceService(IProfessionalExperienceRepository professionalExperienceRepository, IUserRepository userRepository, IOrganisationRepository organizationRepository, ServerEnvUtils serverEnvUtils) {
        _professionalExperienceRepository = professionalExperienceRepository;
        _userRepository = userRepository;
        _organizationRepository = organizationRepository;
        _serverEnvUtils = serverEnvUtils;
    }

    @Override
    public Response<List<BusinessProfessionalExperience>> getAllProfessionalExperiences() {
        try{
            List<ProfessionalExperience> professionalExperienceList = _professionalExperienceRepository.getAll();
            return new Response<>(true, 200, BusinessProfessionalExperience.toBusinessProfessionalExperienceList(professionalExperienceList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessProfessionalExperience>> getProfessionalExperiencesByUserId(Long uid) {
        try{
            List<ProfessionalExperience> professionalExperienceList = _professionalExperienceRepository.getAllByUid(uid);
            return new Response<>(true, 200, BusinessProfessionalExperience.toBusinessProfessionalExperienceList(professionalExperienceList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessProfessionalExperience>> getProfessionalExperiencesByName(String name) {
        try{
            List<ProfessionalExperience> professionalExperienceList = _professionalExperienceRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessProfessionalExperience.toBusinessProfessionalExperienceList(professionalExperienceList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessProfessionalExperience> getProfessionalExperienceById(Long id) {
        try{
            ProfessionalExperience professionalExperience = _professionalExperienceRepository.get(id);
            if (professionalExperience == null){
                return CustomResponse.getItemNotFoundResponse("Professional Experience", id);
            }
            return new Response<>(true, 200, BusinessProfessionalExperience.toBusinessProfessionalExperience(professionalExperience, _serverEnvUtils.getServerUrlPrefix()));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteProfessionalExperienceId(Long id) {
        try{
            Boolean success = _professionalExperienceRepository.delete(id);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "ProfessionalExperience Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }


    @Override
    public Response<BusinessId> createProfessionalExperience(BusinessCreateProfessionalExperience professionalExperience) {
        try{
            if (!professionalExperience.isValid())
                return new Response<>(false, 400, null, professionalExperience.getRequiredFieldsMessage());
            User user = _userRepository.get(professionalExperience.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", professionalExperience.uid);
            }
            Organisation organisation = _organizationRepository.get(professionalExperience.organisationId);
            if (organisation == null){
                return CustomResponse.getItemNotFoundResponse("Professional Institution", professionalExperience.organisationId);
            }

            Long id = _professionalExperienceRepository.add(BusinessCreateProfessionalExperience.toProfessionalExperience(professionalExperience));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create professionalExperience");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return CustomResponse.getItemIsExistentResponse(professionalExperience.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updateProfessionalExperience(Long id, BusinessUpdateProfessionalExperience professionalExperience) {
        try{
            if (!professionalExperience.isValid())
                return new Response<>(false, 400, null, professionalExperience.getRequiredFieldsMessage());
            User user = _userRepository.get(professionalExperience.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", professionalExperience.uid);
            }
            Organisation professionalInstitution = _organizationRepository.get(professionalExperience.organisationId);
            if (professionalInstitution == null){
                return CustomResponse.getItemNotFoundResponse("Professional Institution", professionalExperience.organisationId);
            }

            ProfessionalExperience foundProfessionalExperience = _professionalExperienceRepository.get(id);
            ProfessionalExperience dbProfessionalExperience = BusinessUpdateProfessionalExperience.toProfessionalExperience(id, professionalExperience);
            if (foundProfessionalExperience == null){
                Boolean success = _professionalExperienceRepository.addWithId(dbProfessionalExperience);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With ProfessionalExperience Update");
            }
            Boolean success = _professionalExperienceRepository.update(id, BusinessUpdateProfessionalExperience.toProfessionalExperience(id, professionalExperience));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With ProfessionalExperience Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return CustomResponse.getItemIsExistentResponse(professionalExperience.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
