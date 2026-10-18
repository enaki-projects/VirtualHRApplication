package org.enaki.license.cv_db.presentation.controllers.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateProject;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateProject;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.IProjectService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/cv")
public class ProjectController {
    final IProjectService _projectService;

    public ProjectController(IProjectService projectService) {
        _projectService = projectService;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ResponseEntity<?> getProjects(@RequestParam(required=false) String name, HttpServletRequest request){
        Response<?> response = (name == null) ? _projectService.getAllProjects(): _projectService.getProjectsByName(name);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/projects/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getProjectById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _projectService.getProjectById(id);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value = "/{uid}/projects", method = RequestMethod.GET)
    public ResponseEntity<?> getUserProjects(@PathVariable("uid") Long uid, HttpServletRequest request){
        Response<?> response = _projectService.getProjectsByUserId(uid);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{uid}/projects", method=RequestMethod.POST)
    public ResponseEntity<?> createProject(@PathVariable("uid") Long uid,
                                         @RequestBody BusinessCreateProject project,
                                         HttpServletRequest request){
        project.uid = uid;
        Response<?> response = _projectService.createProject(project);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/projects/{id}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateProject(@PathVariable("id") Long id,
                                         @RequestBody BusinessUpdateProject project,
                                         HttpServletRequest request){
        Response<?> response = _projectService.updateProject(id, project);
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/projects/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProject(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _projectService.deleteProjectId(id);
        return ControllerUtils.makeResponse(response, request);
    }
}