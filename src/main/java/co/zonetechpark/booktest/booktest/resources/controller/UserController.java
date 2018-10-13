package co.zonetechpark.booktest.booktest.resources.controller;

import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.resources.model.request.UserResource;
import co.zonetechpark.booktest.booktest.resources.model.response.UserResponse;
import co.zonetechpark.booktest.booktest.service.UserService;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("create")
    public ResponseEntity<User> createUser(@RequestBody UserResource resource) {
        User user = userService.createUser(resource);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @PutMapping("update")
    public ResponseEntity<Boolean> updateUser(@RequestBody UserResource resource) {
        userService.updateUser(resource);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Boolean> deleteUser(@RequestParam(value = "q") Long userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<Page<User>> viewAllUsers(@QuerydslPredicate(root = User.class)Predicate predicate,
                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "dateCreated");
        Page<User> users = userService.viewAllUsers(predicate, pageable);

        users.getContent().parallelStream().forEach(user -> user.setPassword(null));

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
