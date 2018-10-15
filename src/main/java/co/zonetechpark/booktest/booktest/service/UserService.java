package co.zonetechpark.booktest.booktest.service;

import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.resources.model.request.UserResource;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    User createUser(UserResource resource);
    User updateUser(UserResource resource);
    void deleteUser(Long userId);
    Page<User> viewAllUsers(Predicate predicate, Pageable pageable);
    Optional<User> viewUserById(Long userId);
    Optional<User> viewUserByUsername(String username);
    Boolean toggleUserStatus(Long userId);
}
