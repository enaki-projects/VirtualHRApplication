package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateAward;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessAward;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateAward;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IAwardService;
import org.enaki.license.cv_db.persistence.entities.cv.Award;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.IAwardRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwardService implements IAwardService {
    private final IAwardRepository _awardRepository;
    private final IUserRepository _userRepository;

    public AwardService(IAwardRepository awardRepository, IUserRepository userRepository) {
        _awardRepository = awardRepository;
        _userRepository = userRepository;
    }

    @Override
    public Response<List<BusinessAward>> getAllAwards() {
        try{
            List<Award> awardList = _awardRepository.getAll();
            return new Response<>(true, 200, BusinessAward.toBusinessAwardList(awardList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessAward>> getAwardsByUserId(Long uid) {
        try{
            List<Award> awardList = _awardRepository.getAllByUid(uid);
            return new Response<>(true, 200, BusinessAward.toBusinessAwardList(awardList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessAward>> getAwardsByName(String name) {
        try{
            List<Award> awardList = _awardRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessAward.toBusinessAwardList(awardList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessAward> getAwardById(Long id) {
        try{
            Award award = _awardRepository.get(id);
            if (award == null){
                return CustomResponse.getItemNotFoundResponse("Award", id);
            }
            return new Response<>(true, 200, BusinessAward.toBusinessAward(award));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteAwardId(Long id) {
        try{
            Boolean success = _awardRepository.delete(id);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "Award Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }


    @Override
    public Response<BusinessId> createAward(BusinessCreateAward award) {
        try{
            if (!award.isValid())
                return new Response<>(false, 400, null, award.getRequiredFieldsMessage());

            User user = _userRepository.get(award.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", award.uid);
            }
            Long id = _awardRepository.add(BusinessCreateAward.toAward(award));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create award");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return CustomResponse.getItemIsExistentResponse(award.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updateAward(Long id, BusinessUpdateAward award) {
        try{
            if (!award.isValid())
                return new Response<>(false, 400, null, award.getRequiredFieldsMessage());
            User user = _userRepository.get(award.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", award.uid);
            }

            Award foundAward = _awardRepository.get(id);
            Award dbAward = BusinessUpdateAward.toAward(id, award);
            if (foundAward == null){
                Boolean success = _awardRepository.addWithId(dbAward);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With Award Update");
            }
            Boolean success = _awardRepository.update(id, BusinessUpdateAward.toAward(id, award));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With Award Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return CustomResponse.getItemIsExistentResponse(award.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
