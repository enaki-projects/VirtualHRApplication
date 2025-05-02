package org.enaki.license.cv_db.business.services.world;

import org.enaki.license.cv_db.business.business_models.get.geography.BusinessEducationalInstitution;
import org.enaki.license.cv_db.business.business_models.get.geography.BusinessEducationalInstitutionBasic;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.world.IEducationalService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.geography.Country;
import org.enaki.license.cv_db.persistence.entities.geography.EducationalInstitution;
import org.enaki.license.cv_db.persistence.interfaces.geography.ICountryRepository;
import org.enaki.license.cv_db.persistence.interfaces.geography.IEducationalInstitutionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationalInstitutionService implements IEducationalService {
    private final IEducationalInstitutionRepository _educationalInstitutionRepository;
    private final ICountryRepository _countryRepository;
    private final ServerEnvUtils _serverEnvUtils;

    public EducationalInstitutionService(IEducationalInstitutionRepository educationalInstitutionRepository, ICountryRepository countryRepository, ServerEnvUtils serverEnvUtils) {
        _educationalInstitutionRepository = educationalInstitutionRepository;
        _countryRepository = countryRepository;
        _serverEnvUtils = serverEnvUtils;
    }

    @Override
    public Response<List<BusinessEducationalInstitution>> getEducationalInstitutionsByName(String eduInstitutionName) {
        try{
            List<EducationalInstitution> educationalInstitutionList = _educationalInstitutionRepository.getIEducationalInstitutionsByName(eduInstitutionName);
            return new Response<>(true, 200, BusinessEducationalInstitution.toBusinessEducationInstitutionList(educationalInstitutionList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessEducationalInstitutionBasic>> getEducationalInstitutionsBasicInformationByName(String name) {
        try{
            List<EducationalInstitution> educationalInstitutionList = _educationalInstitutionRepository.getIEducationalInstitutionsByName(name);
            return new Response<>(true, 200, BusinessEducationalInstitutionBasic.toBusinessEducationInstitutionSimplifiedList(educationalInstitutionList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> getEducationalInstitutionsByCountryId(Long id, Boolean withBasicInfo) {
        try{
            Country country = _countryRepository.get(id);
            if (country == null){
                return CustomResponse.getItemNotFoundResponse("Country", id);
            }
            List<EducationalInstitution> educationalInstitutions = _educationalInstitutionRepository.getEducationalInstitutionsByCountryId(id);
            if (withBasicInfo){
                return new Response<>(true, 200, BusinessEducationalInstitutionBasic.
                        toBusinessEducationInstitutionSimplifiedList(educationalInstitutions));
            }
            return new Response<>(true, 200, BusinessEducationalInstitution.
                    toBusinessEducationInstitutionList(educationalInstitutions, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessEducationalInstitution>> getAllEducationalInstitutions() {
        try{
            List<EducationalInstitution> educationalInstitutionList = _educationalInstitutionRepository.getAll();
            return new Response<>(true, 200, BusinessEducationalInstitution.toBusinessEducationInstitutionList(educationalInstitutionList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessEducationalInstitutionBasic>> getAllEducationalInstitutionsBasicInformation() {
        try{
            List<EducationalInstitution> educationalInstitutionList = _educationalInstitutionRepository.getAll();
            return new Response<>(true, 200, BusinessEducationalInstitutionBasic.toBusinessEducationInstitutionSimplifiedList(educationalInstitutionList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }


    @Override
    public Response<BusinessEducationalInstitution> getEducationalInstitution(Long educationalInstitutionId) {
        try{
            EducationalInstitution educationalInstitution = _educationalInstitutionRepository.get(educationalInstitutionId);
            if (educationalInstitution == null){
                return new Response<>(false, 404, null, "Educational Institution Not Found");
            }
            return new Response<>(true, 200, BusinessEducationalInstitution.toBusinessEducationalInstitution(educationalInstitution, _serverEnvUtils.getServerUrlPrefix()));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
