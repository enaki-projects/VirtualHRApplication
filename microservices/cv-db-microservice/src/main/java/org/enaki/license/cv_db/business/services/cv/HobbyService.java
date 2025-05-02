package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateHobby;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessHobby;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateHobby;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IHobbyService;
import org.enaki.license.cv_db.persistence.entities.cv.Hobby;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.IHobbyRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HobbyService implements IHobbyService {
    private final IHobbyRepository _hobbyRepository;
    private final IUserRepository _userRepository;

    public HobbyService(IHobbyRepository hobbyRepository, IUserRepository userRepository) {
        _hobbyRepository = hobbyRepository;
        _userRepository = userRepository;
    }

    @Override
    public Response<List<BusinessHobby>> getAllHobbies() {
        try{
            List<Hobby> hobbyList = _hobbyRepository.getAll();
            return new Response<>(true, 200, BusinessHobby.toBusinessHobbyList(hobbyList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessHobby>> getHobbiesByUserId(Long uid) {
        try{
            List<Hobby> hobbyList = _hobbyRepository.getAllByUid(uid);
            return new Response<>(true, 200, BusinessHobby.toBusinessHobbyList(hobbyList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessHobby>> getHobbiesByName(String name) {
        try{
            List<Hobby> hobbyList = _hobbyRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessHobby.toBusinessHobbyList(hobbyList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessHobby> getHobbyById(Long id) {
        try{
            Hobby hobby = _hobbyRepository.get(id);
            if (hobby == null){
                return CustomResponse.getItemNotFoundResponse("Hobby", id);
            }
            return new Response<>(true, 200, BusinessHobby.toBusinessHobby(hobby));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteHobbyId(Long id) {
        try{
            Boolean success = _hobbyRepository.delete(id);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "Hobby Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }


    @Override
    public Response<BusinessId> createHobby(BusinessCreateHobby hobby) {
        try{
            if (!hobby.isValid())
                return new Response<>(false, 400, null, hobby.getRequiredFieldsMessage());
            User user = _userRepository.get(hobby.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", hobby.uid);
            }

            Long id = _hobbyRepository.add(BusinessCreateHobby.toHobby(hobby));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create hobby");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return CustomResponse.getItemIsExistentResponse(hobby.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updateHobby(Long id, BusinessUpdateHobby hobby) {
        try{
            if (!hobby.isValid())
                return new Response<>(false, 400, null, hobby.getRequiredFieldsMessage());
            User user = _userRepository.get(hobby.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", hobby.uid);
            }

            Hobby foundHobby = _hobbyRepository.get(id);
            Hobby dbHobby = BusinessUpdateHobby.toHobby(id, hobby);
            if (foundHobby == null){
                Boolean success = _hobbyRepository.addWithId(dbHobby);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With Hobby Update");
            }
            Boolean success = _hobbyRepository.update(id, BusinessUpdateHobby.toHobby(id, hobby));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With Hobby Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return CustomResponse.getItemIsExistentResponse(hobby.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
