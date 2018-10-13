package co.zonetechpark.booktest.booktest.resources.controller;

import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.resources.model.request.UserResource;
import co.zonetechpark.booktest.booktest.resources.model.response.UserResponse;
import co.zonetechpark.booktest.booktest.service.UserService;
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
@RequestMapping("/api/v1/user")
@Slf4j
@Api(value = "api/v1/user", description = "Endpoint for user management", tags = "User Management")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @PostMapping("create")
    @ApiOperation(httpMethod = "POST", value = "Resource to create a user", response = UserResponse.class, nickname = "createUser")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Great! User created successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 409, message = "CONFLICT! Name already exist, please choose a different user name"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserResource resource) {
        User user = userService.createUser(resource);
        UserResponse response = new UserResponse();
        response.setUsername(user.getUsername());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setFullName(user.getFullName());
        response.setDateCreated(user.getDateCreated());
        response.setStatus(user.getStatus());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @PutMapping("update")
    @ApiOperation(httpMethod = "PUT", value = "Resource to update a user", responseReference = "true", nickname = "updateUser")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! User updated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 422, message = "Resource not found for the User ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> updateUser(@Valid @RequestBody UserResource resource) {
        userService.updateUser(resource);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @DeleteMapping("delete")
    @ApiOperation(httpMethod = "DELETE", value = "Resource to delete a user", responseReference = "true", nickname = "deleteUser")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! User deleted successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 422, message = "Resource not found for the User ID supplied"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> deleteUser(
            @ApiParam(name = "userId", value = "Provide User ID", required = true)
            @RequestParam(value = "userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @GetMapping("all")
    @ApiOperation(httpMethod = "GET", value = "Resource to view all users", response = User.class, nickname = "viewAllUsers", notes = "You can perform search operations on this method (e.g www.zonetechpark.com/api/v1/user/all?username=author)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View All Users"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Page<User>> viewAllUsers(@QuerydslPredicate(root = User.class) Predicate predicate,
                                                   @ApiParam(name = "page", value = "default number of page", required = true)
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @ApiParam(name = "size", value = "default size on result set", required = true)
                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "dateCreated");
        Page<User> users = userService.viewAllUsers(predicate, pageable);

        users.getContent().parallelStream().forEach(user -> user.setPassword(null));

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("view-user")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a user by User ID", response = UserResponse.class, nickname = "viewUserById")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a User by User Id"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<UserResponse> viewUserById(
            @ApiParam(name = "userId", value = "Provide User ID", required = true)
            @RequestParam(value = "userId") Long userId) {
        Optional<User> optionalUser = userService.viewUserById(userId);
        UserResponse response = new UserResponse();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            response.setUsername(user.getUsername());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setEmail(user.getEmail());
            response.setPhoneNumber(user.getPhoneNumber());
            response.setFullName(user.getFullName());
            response.setDateCreated(user.getDateCreated());
            response.setStatus(user.getStatus());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("view-user-username")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @ApiOperation(httpMethod = "GET", value = "Resource to view a user by User name", response = UserResponse.class, nickname = "viewUserByUsername")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "View a User by Username"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<UserResponse> viewUserByUsername(
            @RequestParam(value = "username") String username) {
        Optional<User> optionalUser = userService.viewUserByUsername(username);
        UserResponse response = new UserResponse();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            response.setUsername(user.getUsername());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setEmail(user.getEmail());
            response.setPhoneNumber(user.getPhoneNumber());
            response.setFullName(user.getFullName());
            response.setDateCreated(user.getDateCreated());
            response.setStatus(user.getStatus());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("status")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @ApiOperation(httpMethod = "PUT", value = "Resource to toggle user status", responseReference = "true", nickname = "toggleUserStatus")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Toggle user status successful"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 403, message = "Sorry, you are unauthorized to access the resources"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<Boolean> toggleUserStatus(
            @ApiParam(name = "userId", value = "Provide User ID", required = true)
            @RequestParam(value = "userId") Long userId) {
        userService.toggleUserStatus(userId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
