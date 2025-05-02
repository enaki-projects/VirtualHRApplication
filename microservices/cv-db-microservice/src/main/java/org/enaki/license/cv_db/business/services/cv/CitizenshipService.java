package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.ICitizenshipService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.geography.Nationality;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.ICitizenshipRepository;
import org.enaki.license.cv_db.persistence.interfaces.geography.INationalityRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitizenshipService implements ICitizenshipService {
    private final ICitizenshipRepository _citizenshipRepository;
    private final INationalityRepository _nationalityRepository;
    private final IUserRepository _userRepository;
    private final ServerEnvUtils _serverEnvUtils;

    public CitizenshipService(ICitizenshipRepository citizenshipRepository, INationalityRepository nationalityRepository, IUserRepository userRepository, ServerEnvUtils serverEnvUtils)  {
        _citizenshipRepository = citizenshipRepository;
        _nationalityRepository = nationalityRepository;
        _userRepository = userRepository;
        _serverEnvUtils = serverEnvUtils;
    }

    @Override
    public Response<List<String>> getUserNationalitiesByUserId(Long uid){
        User user = _userRepository.get(uid);
        if (user == null){
            return CustomResponse.getItemNotFoundResponse("User", uid);
        }
        List<Long> citizenshipList = _citizenshipRepository.getNationalitiesIdsByUid(uid);
        List<String> citizenshipListReferences = citizenshipList
            .stream()
            .map(it -> {
                try {
                    return String.format("%s/api/geography/nationalities/%s", _serverEnvUtils.getServerUrlPrefix(), it);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                return null;
            })
            .collect(Collectors.toList());
        return new Response<>(true, 200, citizenshipListReferences);
    }

    @Override
    public Response<?> deleteUserNationalityId(Long uid, Long nationalityId) {
        User user = _userRepository.get(uid);
        if (user == null){
            return CustomResponse.getItemNotFoundResponse("User", uid);
        }
        Boolean success = _citizenshipRepository.delete(uid, nationalityId);

        if (success){
            return new Response<>(true, 204, null);
        }
        return new Response<>(false, 404, null, String.format("User %s has not the nationality %s", uid, nationalityId));

    }

    @Override
    public Response<?> addUserNationality(Long uid, Long nationalityId) {
        try{
            User user = _userRepository.get(uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", uid);
            }
            Nationality nationality = _nationalityRepository.get(nationalityId);
            if (nationality == null){
                return CustomResponse.getItemNotFoundResponse("Nationality", nationalityId);
            }
            Boolean success = _citizenshipRepository.update(uid, nationalityId);

            if (success){
                return new Response<>(true, 204, null);
            }
            return new Response<>(false, 404, null);
        }

        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, String.format("User %s has already nationality %s", uid, nationalityId));
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }
}
