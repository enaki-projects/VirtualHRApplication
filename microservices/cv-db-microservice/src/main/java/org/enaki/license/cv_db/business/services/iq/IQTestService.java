package org.enaki.license.cv_db.business.services.iq;

import org.enaki.license.cv_db.business.business_models.create.iq.BusinessCreateIQTest;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.iq.BusinessBasicIQTest;
import org.enaki.license.cv_db.business.business_models.get.iq.BusinessIQTest;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.iq.IIQTestService;
import org.enaki.license.cv_db.persistence.entities.iq.IQTest;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.ICVRepository;
import org.enaki.license.cv_db.persistence.interfaces.iq.IIQTestRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IQTestService implements IIQTestService {
    private final ICVRepository _cvRepository;
    private final IUserRepository _userRepository;
    private final IIQTestRepository _iqTestRepository;

    public IQTestService(ICVRepository cvRepository, IUserRepository userRepository, IIQTestRepository iqTestRepository) {
        _cvRepository = cvRepository;
        _userRepository = userRepository;
        _iqTestRepository = iqTestRepository;
    }

    @Override
    public Response<?> getAllIQTests(Boolean basicInfo) {
        try{
            List<IQTest> iqTestList = _iqTestRepository.getAll();
            return new Response<>(true, 200, (basicInfo)?
                    BusinessBasicIQTest.toBusinessBasicIQTestList(iqTestList):
                    BusinessIQTest.toBusinessIQTestList(iqTestList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> getIQTestById(Long iqTestId, Boolean basicInfo) {
        try{
            IQTest iqTest = _iqTestRepository.get(iqTestId);
            if (iqTest == null){
                return CustomResponse.getItemNotFoundResponse("IQ Test", iqTestId);
            }
            return new Response<>(true, 200, (basicInfo)?
                    BusinessBasicIQTest.toBusinessBasicIQTest(iqTest):
                    BusinessIQTest.toBusinessIQTest(iqTest));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> getLatestIQTestByUid(Long uid, Boolean basicInfo) {
        try{
            IQTest iqTest = _iqTestRepository.getLatestByUid(uid);
            if (iqTest == null){
                return CustomResponse.getItemNotFoundResponse("Latest IQ Test Of User", uid);
            }
            return new Response<>(true, 200, (basicInfo)?
                    BusinessBasicIQTest.toBusinessBasicIQTest(iqTest):
                    BusinessIQTest.toBusinessIQTest(iqTest));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> getIQTestsByUid(Long uid, Boolean basicInfo) {
        try{
            User user = _userRepository.get(uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", uid);
            }

            List<IQTest> iqTestList = _iqTestRepository.getAllByUid(uid);
            return new Response<>(true, 200, (basicInfo)?
                    BusinessBasicIQTest.toBusinessBasicIQTestList(iqTestList):
                    BusinessIQTest.toBusinessIQTestList(iqTestList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteIQTestById(Long iqTestId) {
        try{
            Boolean success = _iqTestRepository.delete(iqTestId);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "IQ Test Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessId> addIQTest(BusinessCreateIQTest iqTest) {
        try{
            if (!iqTest.isValid())
                return new Response<>(false, 400, null, iqTest.getRequiredFieldsMessage());
            User user = _userRepository.get(iqTest.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", iqTest.uid);
            }

            Long id = _iqTestRepository.add(BusinessCreateIQTest.toIQTest(iqTest));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create iq test");
            }
            if (!_cvRepository.updateIqTestStatus(iqTest.uid, iqTest.iq)){
                _iqTestRepository.delete(id);
                return new Response<>(false, 400, null, "Can't update cv of the user after taking the iq test");
            }

            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, iqTest.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> addPermissionToRetakeTheTest(Long uid) {
        try{
            User user = _userRepository.get(uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", uid);
            }

            if (!_cvRepository.addPermissionToRetakeTheTest(uid)){
                return new Response<>(true, 500, "User did not receive the permission to retake the test");
            };
            return new Response<>(true, 204, null);
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> requestPermissionToRetakeTheTest(Long uid) {
        try{
            User user = _userRepository.get(uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", uid);
            }

            if (!_cvRepository.requestPermissionToRetakeTheTest(uid)){
                return new Response<>(true, 500, "User can not request permission to retake the test");
            };
            return new Response<>(true, 204, null);
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> restrictPermissionToTakeTheTest(Long uid) {
        try{
            User user = _userRepository.get(uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", uid);
            }

            _cvRepository.restrictPermissionToTakeTheTest(uid);
            return new Response<>(true, 204, null);
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
