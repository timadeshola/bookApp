package co.zonetechpark.booktest.booktest.resources.controller;

import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import co.zonetechpark.booktest.booktest.resources.model.request.RoleResource;
import co.zonetechpark.booktest.booktest.resources.model.response.RoleResponse;
import co.zonetechpark.booktest.booktest.service.RoleService;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/role")
@Slf4j
@Api(value = "api/v1/role", description = "Endpoint for role management", tags = "Role Management")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_EDITOR')")
    @PostMapping("create")
    @ApiOperation(httpMethod = "POST", value = "Resource to create a role", response = RoleResponse.class, nickname = "createRole")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! Role created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different role name"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleResource resource) {
        Role role = roleService.createRole(resource);
        RoleResponse response = new RoleResponse();
        response.setName(role.getName());
        response.setStatus(role.getStatus());
        response.setDateCreated(role.getDateCreated());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_EDITOR')")
    @PutMapping("update")
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a role", response = RoleResponse.class, nickname = "updateRole")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Role updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Role ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody RoleResource resource) {
        Role role = roleService.updateRole(resource);
        RoleResponse response = new RoleResponse();
        response.setName(role.getName());
        response.setStatus(role.getStatus());
        response.setDateCreated(role.getDateCreated());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @DeleteMapping("delete")
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a role", responseReference = "true", nickname = "deleteRole")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! Role deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 408, message = "Validation failed"),
            @ApiResponse(code = 422, message = "Resource not found for the Role ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteRole(
            @ApiParam(name = "roleId", value = "Provide Role ID", required = true)
            @RequestParam(value = "roleId") Long roleId) {
        roleService.deleteRole(roleId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_EDITOR')")
    @GetMapping("all")
    @ApiOperation(httpMethod = "GET", value = "Resource to view all roles", response = Role.class, nickname = "viewAllRoles", notes = "You can perform search operations on this method (e.g www.zonetechpark.com/api/v1/role/all?name=author)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View All Roles"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Page<Role>> viewAllRoles(@QuerydslPredicate(root = Role.class)Predicate predicate,
                                                   @ApiParam(name = "page", value = "default number of page", required = true)
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @ApiParam(name = "size", value = "default size on result set", required = true)
                                                   @RequestParam(value = "size", defaultValue = "10") int size ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "dateCreated");
        Page<Role> roles = roleService.viewAllRoles(predicate, pageable);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_EDITOR')")
    @GetMapping("view-role")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a role by Role ID", response = RoleResponse.class, nickname = "viewRoleById")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a Role"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<RoleResponse> viewRoleById(
            @ApiParam(name = "roleId", value = "Provide Role ID", required = true)
            @RequestParam(value = "roleId") Long roleId) {
        Optional<Role> optionalRole = roleService.viewRoleById(roleId);
        RoleResponse response = new RoleResponse();
        if(optionalRole.isPresent()) {
            Role role = optionalRole.get();
            response.setName(role.getName());
            response.setStatus(role.getStatus());
            response.setDateCreated(role.getDateCreated());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ROLE_AUTHOR', 'ROLE_EDITOR')")
    @GetMapping("view-role-name")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a role by Role Name", response = RoleResponse.class, nickname = "viewRoleByName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a Role"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<RoleResponse> viewRoleByName(
            @ApiParam(name = "name", value = "Provide Role Name", required = true)
            @RequestParam(value = "name") String name) {
        Optional<Role> optionalRole = roleService.viewRoleByName(name);
        RoleResponse response = new RoleResponse();
        if(optionalRole.isPresent()) {
            Role role = optionalRole.get();
            response.setName(role.getName());
            response.setStatus(role.getStatus());
            response.setDateCreated(role.getDateCreated());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PutMapping("status")
    @ApiOperation(httpMethod = "PUT", value = "Resource to toggle role status", responseReference = "true", nickname = "toggleRoleStatus")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Toggle role status successful"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> toggleRoleStatus(
            @ApiParam(name = "roleId", value = "Provide Role ID", required = true)
            @RequestParam(value = "roleId") Long roleId) {
        roleService.toggleRoleStatus(roleId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
