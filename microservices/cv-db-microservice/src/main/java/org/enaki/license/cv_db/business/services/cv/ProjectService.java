package org.enaki.license.cv_db.business.services.cv;


import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateProject;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessProject;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateProject;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IProjectService;
import org.enaki.license.cv_db.persistence.entities.cv.Project;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.IProjectRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService implements IProjectService {
    private final IProjectRepository _projectRepository;
    private final IUserRepository _userRepository;

    public ProjectService(IProjectRepository projectRepository, IUserRepository userRepository) {
        _projectRepository = projectRepository;
        _userRepository = userRepository;
    }

    @Override
    public Response<List<BusinessProject>> getAllProjects() {
        try{
            List<Project> projectList = _projectRepository.getAll();
            return new Response<>(true, 200, BusinessProject.toBusinessProjectList(projectList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessProject>> getProjectsByUserId(Long uid) {
        try{
            List<Project> projectList = _projectRepository.getAllByUid(uid);
            return new Response<>(true, 200, BusinessProject.toBusinessProjectList(projectList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessProject>> getProjectsByName(String name) {
        try{
            List<Project> projectList = _projectRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessProject.toBusinessProjectList(projectList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessProject> getProjectById(Long id) {
        try{
            Project project = _projectRepository.get(id);
            if (project == null){
                return CustomResponse.getItemNotFoundResponse("Project", id);
            }
            return new Response<>(true, 200, BusinessProject.toBusinessProject(project));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteProjectId(Long id) {
        try{
            Boolean success = _projectRepository.delete(id);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "Project Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }


    @Override
    public Response<BusinessId> createProject(BusinessCreateProject project) {
        try{
            if (!project.isValid())
                return new Response<>(false, 400, null, project.getRequiredFieldsMessage());
            User user = _userRepository.get(project.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", project.uid);
            }

            Long id = _projectRepository.add(BusinessCreateProject.toProject(project));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create project");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, project.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updateProject(Long id, BusinessUpdateProject project) {
        try{
            if (!project.isValid())
                return new Response<>(false, 400, null, project.getRequiredFieldsMessage());
            User user = _userRepository.get(project.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", project.uid);
            }

            Project foundProject = _projectRepository.get(id);
            Project dbProject = BusinessUpdateProject.toProject(id, project);
            if (foundProject == null){
                Boolean success = _projectRepository.addWithId(dbProject);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With Project Update");
            }
            Boolean success = _projectRepository.update(id, BusinessUpdateProject.toProject(id, project));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With Project Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return new Response<>(false, 400, null, project.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
