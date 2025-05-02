package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateOrganisation;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessOrganisation;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateOrganisation;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IOrganisationService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.cv.Organisation;
import org.enaki.license.cv_db.persistence.entities.geography.City;
import org.enaki.license.cv_db.persistence.interfaces.cv.IOrganisationRepository;
import org.enaki.license.cv_db.persistence.interfaces.geography.ICityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganisationService implements IOrganisationService {
    private final IOrganisationRepository _organizationRepository;
    private final ICityRepository _cityRepository;
    private final ServerEnvUtils _serverEnvUtils;

    public OrganisationService(IOrganisationRepository organizationRepository, ICityRepository cityRepository, ServerEnvUtils serverEnvUtils) {
        _organizationRepository = organizationRepository;
        _cityRepository = cityRepository;
        _serverEnvUtils = serverEnvUtils;
    }

    @Override
    public Response<List<BusinessOrganisation>> getAllOrganisations() {
        try{
            List<Organisation> organisationList = _organizationRepository.getAll();
            return new Response<>(true, 200, BusinessOrganisation.toBusinessOrganizationList(organisationList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessOrganisation>> getOrganisationsByName(String name) {
        try{
            List<Organisation> organisationList = _organizationRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessOrganisation.toBusinessOrganizationList(organisationList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessOrganisation> getOrganisationById(Long id) {
        try{
            Organisation organisation = _organizationRepository.get(id);
            if (organisation == null){
                return CustomResponse.getItemNotFoundResponse("Organization", id);
            }
            return new Response<>(true, 200, BusinessOrganisation.toBusinessOrganization(organisation, _serverEnvUtils.getServerUrlPrefix()));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteOrganisationId(Long id) {
        try{
            Boolean success = _organizationRepository.delete(id);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "Organization Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }


    @Override
    public Response<BusinessId> createOrganisation(BusinessCreateOrganisation organisation) {
        try{
            if (!organisation.isValid())
                return new Response<>(false, 400, null, organisation.getRequiredFieldsMessage());
            City city = _cityRepository.get(organisation.cityId);
            if (city == null){
                return CustomResponse.getItemNotFoundResponse("City", organisation.cityId);
            }

            Long id = _organizationRepository.add(BusinessCreateOrganisation.toOrganization(organisation));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create organisation");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, organisation.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updateOrganisation(Long id, BusinessUpdateOrganisation organisation) {
        try{
            if (!organisation.isValid())
                return new Response<>(false, 400, null, organisation.getRequiredFieldsMessage());
            City city = _cityRepository.get(organisation.cityId);
            if (city == null){
                return CustomResponse.getItemNotFoundResponse("City", organisation.cityId);
            }

            Organisation foundOrganisation = _organizationRepository.get(id);
            Organisation dbOrganisation = BusinessUpdateOrganisation.toOrganization(id, organisation);
            if (foundOrganisation == null){
                Boolean success = _organizationRepository.addWithId(dbOrganisation);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With Organization Update");
            }
            Boolean success = _organizationRepository.update(id, BusinessUpdateOrganisation.toOrganization(id, organisation));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With Organization Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, organisation.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessOrganisation>> getOrganisationsByCityId(Long id) {
        try{
            City city = _cityRepository.get(id);
            if (city == null){
                return CustomResponse.getItemNotFoundResponse("City", id);
            }

            List<Organisation> organisationList = _organizationRepository.getByCityId(id);
            return new Response<>(true, 200, BusinessOrganisation.toBusinessOrganizationList(organisationList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessOrganisation>> getOrganisationsByNameAndCityId(String name, Long cityId) {
        try{
            City city = _cityRepository.get(cityId);
            if (city == null){
                return CustomResponse.getItemNotFoundResponse("City", cityId);
            }

            List<Organisation> organisationList = _organizationRepository.getAllByNameAndCityId(name, cityId);
            return new Response<>(true, 200, BusinessOrganisation.toBusinessOrganizationList(organisationList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
