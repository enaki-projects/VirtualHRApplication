package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateUserLanguage;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessUserLanguage;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateUserLanguage;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IUserLanguageService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.cv.UserLanguage;
import org.enaki.license.cv_db.persistence.entities.geography.Language;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.IUserLanguageRepository;
import org.enaki.license.cv_db.persistence.interfaces.geography.ILanguageRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLanguageService implements IUserLanguageService {
    private final IUserLanguageRepository _userLanguageRepository;
    private final IUserRepository _userRepository;
    private final ILanguageRepository _languageRepository;
    private final ServerEnvUtils _serverEnvUtils;


    public UserLanguageService(IUserLanguageRepository userLanguageRepository, IUserRepository userRepository, ILanguageRepository languageRepository, ServerEnvUtils serverEnvUtils) {
        _userLanguageRepository = userLanguageRepository;
        _userRepository = userRepository;
        _languageRepository = languageRepository;
        _serverEnvUtils = serverEnvUtils;
    }

    @Override
    public Response<List<BusinessUserLanguage>> getAllUserLanguages() {
        try{
            List<UserLanguage> userLanguageList = _userLanguageRepository.getAll();
            return new Response<>(true, 200, BusinessUserLanguage.toBusinessUserLanguageList(userLanguageList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessUserLanguage>> getUserLanguagesByUserId(Long uid) {
        try{
            List<UserLanguage> userLanguageList = _userLanguageRepository.getAllByUid(uid);
            return new Response<>(true, 200, BusinessUserLanguage.toBusinessUserLanguageList(userLanguageList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessUserLanguage>> getUserLanguagesByName(String name) {
        try{
            List<UserLanguage> userLanguageList = _userLanguageRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessUserLanguage.toBusinessUserLanguageList(userLanguageList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessUserLanguage> getUserLanguageById(Long id) {
        try{
            UserLanguage userLanguage = _userLanguageRepository.get(id);
            if (userLanguage == null){
                return CustomResponse.getItemNotFoundResponse("User Language", id);
            }
            return new Response<>(true, 200, BusinessUserLanguage.toBusinessUserLanguage(userLanguage, _serverEnvUtils.getServerUrlPrefix()));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteUserLanguageId(Long id) {
        try{
            Boolean success = _userLanguageRepository.delete(id);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "UserLanguage Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }


    @Override
    public Response<BusinessId> createUserLanguage(BusinessCreateUserLanguage userLanguage) {
        try{
            if (!userLanguage.isValid())
                return new Response<>(false, 400, null, userLanguage.getRequiredFieldsMessage());
            User user = _userRepository.get(userLanguage.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", userLanguage.uid);
            }
            Language language = _languageRepository.get(userLanguage.languageId);
            if (language == null){
                return CustomResponse.getItemNotFoundResponse("Language", userLanguage.languageId);
            }

            Long id = _userLanguageRepository.add(BusinessCreateUserLanguage.toUserLanguage(userLanguage));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create userLanguage");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, userLanguage.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updateUserLanguage(Long id, BusinessUpdateUserLanguage userLanguage) {
        try{
            if (!userLanguage.isValid())
                return new Response<>(false, 400, null, userLanguage.getRequiredFieldsMessage());
            User user = _userRepository.get(userLanguage.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", userLanguage.uid);
            }
            Language language = _languageRepository.get(userLanguage.languageId);
            if (language == null){
                return CustomResponse.getItemNotFoundResponse("Language", userLanguage.languageId);
            }

            UserLanguage foundUserLanguage = _userLanguageRepository.get(id);
            UserLanguage dbUserLanguage = BusinessUpdateUserLanguage.toUserLanguage(id, userLanguage);
            if (foundUserLanguage == null){
                Boolean success = _userLanguageRepository.addWithId(dbUserLanguage);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With UserLanguage Update");
            }
            Boolean success = _userLanguageRepository.update(id, BusinessUpdateUserLanguage.toUserLanguage(id, userLanguage));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With UserLanguage Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, userLanguage.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
