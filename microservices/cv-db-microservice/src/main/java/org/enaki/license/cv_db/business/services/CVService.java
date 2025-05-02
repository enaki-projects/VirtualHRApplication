package org.enaki.license.cv_db.business.services;

import org.enaki.license.cv_db.business.business_models.create.BusinessCreateCV;
import org.enaki.license.cv_db.business.business_models.get.BusinessCV;
import org.enaki.license.cv_db.business.business_models.get.BusinessCVReferences;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.update.BusinessUpdateCV;
import org.enaki.license.cv_db.business.companion.SkillType;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.ICVService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.CV;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.*;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CVService implements ICVService {

    private final ICVRepository _cvRepository;
    private final IUserRepository _userRepository;
    private final ILocationRepository _locationRepository;
    private final ICitizenshipRepository _citizenshipRepository;
    private final IAwardRepository _awardRepository;
    private final IHobbyRepository _hobbyRepository;
    private final IProjectRepository _projectRepository;
    private final IPhoneNumberRepository _phoneNumberRepository;
    private final IPublicationRepository _publicationRepository;
    private final IUserLanguageRepository _userLanguageRepository;
    private final IEducationalExperienceRepository _educationalExperienceRepository;
    private final IProfessionalExperienceRepository _professionalExperienceRepository;
    private final ISkillRepository _skillRepository;
    private final ServerEnvUtils _serverEnvUtils;

    public CVService(ICVRepository cvRepository, IUserRepository userRepository, ILocationRepository locationRepository, ICitizenshipRepository citizenshipRepository, IAwardRepository awardRepository, IHobbyRepository hobbyRepository, IProjectRepository projectRepository, IPhoneNumberRepository phoneNumberRepository, IPublicationRepository publicationRepository, IUserLanguageRepository userLanguageRepository, IEducationalExperienceRepository educationalExperienceRepository, ISkillRepository skillRepository, IProfessionalExperienceRepository professionalExperienceRepository, ServerEnvUtils serverEnvUtils) {
        _cvRepository = cvRepository;
        _userRepository = userRepository;
        _locationRepository = locationRepository;
        _citizenshipRepository = citizenshipRepository;
        _awardRepository = awardRepository;
        _hobbyRepository = hobbyRepository;
        _projectRepository = projectRepository;
        _phoneNumberRepository = phoneNumberRepository;
        _publicationRepository = publicationRepository;
        _userLanguageRepository = userLanguageRepository;
        _educationalExperienceRepository = educationalExperienceRepository;
        _professionalExperienceRepository = professionalExperienceRepository;
        _skillRepository = skillRepository;
        _serverEnvUtils = serverEnvUtils;
    }


    @Override
    public Response<List<BusinessCV>> getAllCVs() {
        try{
            List<CV> cvList = _cvRepository.getAll();
            List<BusinessCV> businessCVList = new ArrayList<>();
            for(CV cv: cvList){
                BusinessCV businessCV = BusinessCV.toBusinessCV(cv);
                businessCV.cvReferences = getBusinessCVReferences(cv.uid);

                // TODO professional and iqScore
                businessCVList.add(businessCV);
            }
            return new Response<>(true, 200, businessCVList);
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> getCVByUserId(Long uid) {
        try{
            CV cv = _cvRepository.get(uid);
            BusinessCV businessCV = BusinessCV.toBusinessCV(cv);
            businessCV.cvReferences = getBusinessCVReferences(cv.uid);
            return new Response<>(true, 200, businessCV);
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> createCV(BusinessCreateCV cv) {
        try{
            if (!cv.isValid())
                return new Response<>(false, 400, null, cv.getRequiredFieldsMessage());

            User user = _userRepository.get(cv.uid);
            if (user == null){
                return new Response<>(false, 400, null, "User not found");
            }

            Long uid = _cvRepository.add(BusinessCreateCV.toCV(cv));
            if (uid == null){
                return new Response<>(false, 400, null, "Can't create cv");
            }
            return new Response<>(true, 201, new BusinessId(uid));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, cv.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<CV> getCVByEmail(String email) {
        return null;
    }

    @Override
    public Response<?> updateCV(Long uid, BusinessUpdateCV cv) {
        try{
            if (!cv.isValid())
                return new Response<>(false, 400, null, cv.getRequiredFieldsMessage());
            CV foundCV = _cvRepository.get(uid);
            CV dbCV = BusinessUpdateCV.toCV(uid, cv);

            if (foundCV == null) {
                dbCV.uid = uid;
                Boolean success = _cvRepository.add(dbCV) > 0;
                return new Response<>(success, success ? 201 : 500, null, success ? "" : "Something Went Wrong With CV Update");
            }
            CV toUpdateCV = BusinessUpdateCV.toCV(uid, cv);
            Boolean success = _cvRepository.update(uid, toUpdateCV);
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With CV Update");

        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return CustomResponse.getItemIsExistentResponse(cv.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteCV(Long uid) {
        try{
            Boolean success = _cvRepository.delete(uid);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "CV Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    private BusinessCVReferences getBusinessCVReferences(Long uid) throws UnknownHostException {
        BusinessCVReferences cvReferences = new BusinessCVReferences(_serverEnvUtils.getServerUrlPrefix());
        cvReferences.setLocations(_locationRepository.getLocationsIdsByUid(uid));
        cvReferences.setCitizenship(_citizenshipRepository.getNationalitiesIdsByUid(uid));
        cvReferences.setAwards(_awardRepository.getAwardsIdsByUid(uid));
        cvReferences.setHobbies(_hobbyRepository.getHobbiesIdsByUid(uid));
        cvReferences.setPublications(_publicationRepository.getPublicationsIdsByUid(uid));
        cvReferences.setProjects(_projectRepository.getProjectsIdsByUid(uid));
        cvReferences.setUserLanguages(_userLanguageRepository.getUserLanguagesIdsByUid(uid));
        cvReferences.setEducationalExperiences(_educationalExperienceRepository.getEducationalExperiencesIdsByUid(uid));
        cvReferences.setPhoneNumbers(_phoneNumberRepository.getPhoneNumbersIdsByUid(uid));
        cvReferences.setProfessionalExperiences(_professionalExperienceRepository.getProfessionalExperiencesIdsByUid(uid));

        _skillRepository.setTableName(SkillType.INTERPERSONAL.toString());
        cvReferences.setUserInterpersonalSkills(_skillRepository.getSkillsIdsByUid(uid));

        _skillRepository.setTableName(SkillType.DIGITAL.toString());
        cvReferences.setUserDigitalSkills(_skillRepository.getSkillsIdsByUid(uid));

        _skillRepository.setTableName(SkillType.LEADERSHIP.toString());
        cvReferences.setUserLeadershipSkills(_skillRepository.getSkillsIdsByUid(uid));

        _skillRepository.setTableName(SkillType.ORGANISATIONAL.toString());
        cvReferences.setUserOrganisationalSkills(_skillRepository.getSkillsIdsByUid(uid));

        return cvReferences;
    }
}
