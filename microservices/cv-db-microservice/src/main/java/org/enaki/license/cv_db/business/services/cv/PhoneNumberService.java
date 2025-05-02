package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreatePhoneNumber;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessPhoneNumber;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdatePhoneNumber;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IPhoneNumberService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.cv.PhoneNumber;
import org.enaki.license.cv_db.persistence.entities.geography.Country;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.IPhoneNumberRepository;
import org.enaki.license.cv_db.persistence.interfaces.geography.ICountryRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneNumberService implements IPhoneNumberService {
    private final IPhoneNumberRepository _phoneNumberRepository;
    private final IUserRepository _userRepository;
    private final ICountryRepository _countryRepository;
    private final ServerEnvUtils _serverEnvUtils;

    public PhoneNumberService(IPhoneNumberRepository phoneNumberRepository, IUserRepository userRepository, ICountryRepository countryRepository, ServerEnvUtils serverEnvUtils) {
        _phoneNumberRepository = phoneNumberRepository;
        _userRepository = userRepository;
        _countryRepository = countryRepository;
        _serverEnvUtils = serverEnvUtils;
    }

    @Override
    public Response<List<BusinessPhoneNumber>> getAllPhoneNumbers() {
        try{
            List<PhoneNumber> phoneNumberList = _phoneNumberRepository.getAll();
            return new Response<>(true, 200, BusinessPhoneNumber.toBusinessPhoneNumberList(phoneNumberList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessPhoneNumber>> getPhoneNumbersByUserId(Long uid) {
        try{
            List<PhoneNumber> phoneNumberList = _phoneNumberRepository.getAllByUid(uid);
            return new Response<>(true, 200, BusinessPhoneNumber.toBusinessPhoneNumberList(phoneNumberList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessPhoneNumber>> getPhoneNumbersByName(String name) {
        try{
            List<PhoneNumber> phoneNumberList = _phoneNumberRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessPhoneNumber.toBusinessPhoneNumberList(phoneNumberList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessPhoneNumber> getPhoneNumberById(Long id) {
        try{
            PhoneNumber phoneNumber = _phoneNumberRepository.get(id);
            if (phoneNumber == null){
                return CustomResponse.getItemNotFoundResponse("Phone Number", id);
            }
            return new Response<>(true, 200, BusinessPhoneNumber.toBusinessPhoneNumber(phoneNumber, _serverEnvUtils.getServerUrlPrefix()));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deletePhoneNumberId(Long id) {
        try{
            Boolean success = _phoneNumberRepository.delete(id);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "PhoneNumber Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }


    @Override
    public Response<BusinessId> createPhoneNumber(BusinessCreatePhoneNumber phoneNumber) {
        try{
            if (!phoneNumber.isValid())
                return new Response<>(false, 400, null, phoneNumber.getRequiredFieldsMessage());
            Country country = _countryRepository.get(phoneNumber.countryId);
            if (country == null){
                return CustomResponse.getItemNotFoundResponse("Country", phoneNumber.countryId);
            }

            User user = _userRepository.get(phoneNumber.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", phoneNumber.uid);
            }
            Long id = _phoneNumberRepository.add(BusinessCreatePhoneNumber.toPhoneNumber(phoneNumber));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create phoneNumber");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, phoneNumber.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updatePhoneNumber(Long id, BusinessUpdatePhoneNumber phoneNumber) {
        try{
            if (!phoneNumber.isValid())
                return new Response<>(false, 400, null, phoneNumber.getRequiredFieldsMessage());
            Country country = _countryRepository.get(phoneNumber.countryId);
            if (country == null){
                return CustomResponse.getItemNotFoundResponse("Country", phoneNumber.countryId);
            }

            User user = _userRepository.get(phoneNumber.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", phoneNumber.uid);
            }
            PhoneNumber foundPhoneNumber = _phoneNumberRepository.get(id);
            PhoneNumber dbPhoneNumber = BusinessUpdatePhoneNumber.toPhoneNumber(id, phoneNumber);
            if (foundPhoneNumber == null){
                Boolean success = _phoneNumberRepository.addWithId(dbPhoneNumber);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With PhoneNumber Update");
            }
            Boolean success = _phoneNumberRepository.update(id, BusinessUpdatePhoneNumber.toPhoneNumber(id, phoneNumber));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With PhoneNumber Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, phoneNumber.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
