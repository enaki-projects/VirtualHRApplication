package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateSkill;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessSkill;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateSkill;
import org.enaki.license.cv_db.business.companion.SkillType;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.ISkillService;
import org.enaki.license.cv_db.persistence.entities.cv.Skill;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.ISkillRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService implements ISkillService {

    protected final ISkillRepository _skillRepository;
    protected final IUserRepository _userRepository;

    public SkillService(ISkillRepository skillRepository, IUserRepository userRepository) {
        _skillRepository = skillRepository;
        _userRepository = userRepository;
    }

    @Override
    public void setSkillType(SkillType skillType) {
        _skillRepository.setTableName(skillType.toString());
    }

    @Override
    public Response<List<BusinessSkill>> getAllSkills() {
        try{
            List<Skill> skillList = _skillRepository.getAll();
            return new Response<>(true, 200, BusinessSkill.toBusinessSkillList(skillList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessSkill>> getSkillsByUserId(Long uid) {
        try{
            User user = _userRepository.get(uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", uid);
            }
            List<Skill> skillList = _skillRepository.getAllByUid(uid);
            return new Response<>(true, 200, BusinessSkill.toBusinessSkillList(skillList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessSkill>> getSkillsByName(String name) {
        try{
            List<Skill> skillList = _skillRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessSkill.toBusinessSkillList(skillList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessSkill> getSkillById(Long id) {
        try{
            Skill skill = _skillRepository.get(id);
            if (skill == null){
                return new Response<>(false, 404, null, "Skill Not Found");
            }
            return new Response<>(true, 200, BusinessSkill.toBusinessSkill(skill));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteSkillId(Long id) {
        try{
            Boolean success = _skillRepository.delete(id);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "Skill Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessId> createSkill(BusinessCreateSkill skill) {
        try{
            if (!skill.isValid())
                return new Response<>(false, 400, null, skill.getRequiredFieldsMessage());

            Long id = _skillRepository.add(BusinessCreateSkill.toSkill(skill));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create skill");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, skill.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updateSkill(Long id, BusinessUpdateSkill skill) {
        try{
            if (!skill.isValid())
                return new Response<>(false, 400, null, skill.getRequiredFieldsMessage());

            Skill foundSkill = _skillRepository.get(id);
            Skill dbSkill = BusinessUpdateSkill.toSkill(id, skill);
            if (foundSkill == null){
                Boolean success = _skillRepository.addWithId(dbSkill);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With Skill Update");
            }
            Boolean success = _skillRepository.update(id, BusinessUpdateSkill.toSkill(id, skill));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With Skill Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, skill.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> addUserSkill(Long uid, String skillName) {
        try{
            User user = _userRepository.get(uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", uid);
            }

            Skill skill = _skillRepository.getSkillByName(skillName);
            Long id = (skill == null) ? _skillRepository.add(new Skill(-1L, skillName, null)): skill.skillId;

            Boolean success = _skillRepository.addUserSkill(uid, id);

            return new Response<>(success, success ? 200: 500, null,  success ? "": "Something Went Wrong With User Skill Add");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, String.format("User %s has already skill %s.", uid, skillName));
        }
        catch(Exception e){
            return new Response<>(false, 500, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteUserSkill(Long uid, String skillName) {
        try{
            User user = _userRepository.get(uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", uid);
            }

            Skill skill = _skillRepository.getSkillByName(skillName);
            if (skill == null){
                return CustomResponse.getItemNotFoundResponse(_skillRepository.getTableName(), skillName);
            }

            Boolean success = _skillRepository.deleteUserSkill(uid, skill.skillId);

            return new Response<>(success, success ? 200: 404, null,  success ? "": String.format("Skill %s for user %s not found.", skillName, uid));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessSkill> getSkillByName(String name) {
        try{
            Skill skill = _skillRepository.getSkillByName(name);
            if (skill == null){
                return CustomResponse.getItemNotFoundResponse("Skill", name);
            }
            return new Response<>(true, 200, BusinessSkill.toBusinessSkill(skill));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
