package org.enaki.license.cv_db.presentation.controllers.user;

import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.users.IRoleService;
import org.enaki.license.cv_db.presentation.utils.ControllerUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="api/roles")
public class RoleController {
    private final IRoleService _roleService;

    public RoleController(IRoleService roleService) {
        _roleService = roleService;
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<?> getRoles(HttpServletRequest request){
        Response<?> response = _roleService.getAllRoles();
        return ControllerUtils.makeResponse(response, request);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getRoleById(@PathVariable("id") Long id, HttpServletRequest request){
        Response<?> response = _roleService.getRoleById(id);
        return ControllerUtils.makeResponse(response, request);
    }
}
