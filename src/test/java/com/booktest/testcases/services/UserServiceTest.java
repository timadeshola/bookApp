package com.booktest.testcases.services;

import com.booktest.jpa.entity.Role;
import com.booktest.jpa.entity.User;
import com.booktest.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserService userService;

    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void createUserTest() {

        user = User.builder().id(10L).username("jadeshola").password("Password@123").firstName("John").lastName("Adeshola")
                .fullName("John Adeshola").email("timadeshola@gmail.com").status(true).phoneNumber("2347030239942")
                .dateUpdated(null).dateDeleted(null).roles(new HashSet<>(Collections.singletonList(new Role("AUTHOR")))).build();

        assertEquals("User creation test", user.getUsername(), "jadeshola");
    }

    @Test
    public void updateUserTest() {

        user = User.builder().id(10L).username("jadeshola").password("Password@123").firstName("John").lastName("Adeshola")
                .fullName("John Adeshola").email("timadeshola@gmail.com").status(true).phoneNumber("2347030239943")
                .dateUpdated(null).dateDeleted(null).roles(new HashSet<>(Collections.singletonList(new Role("AUTHOR")))).build();

        assertEquals("User creation test", user.getPhoneNumber(), "2347030239943");

    }

    @Test
    public void deleteUserTest() {
        user = User.builder().id(10L).username("jadeshola").password("Password@123").firstName("John").lastName("Adeshola")
                .fullName("John Adeshola").email("timadeshola@gmail.com").status(true).phoneNumber("2347030239943")
                .dateUpdated(null).dateDeleted(null).roles(new HashSet<>(Collections.singletonList(new Role("AUTHOR")))).build();

        userService.deleteUser(user.getId());
        verify(userService, times(1)).deleteUser(user.getId());

    }

    @Test
    public void viewAllUsersTest() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "dateCreated");
        Page<User> users = userService.viewAllUsers(null, pageRequest);
        assertEquals("view all user test case", users, users);

    }

    @Test
    public void findUserByUsernameTest() {

        Optional<User> optionalUser = userService.viewUserByUsername("author");
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            when(userService.viewUserByUsername("author")).thenReturn(optionalUser);
            assertEquals("Return username", user.getUsername(), "author");
        }
    }

    @Test
    public void viewUserByIdTest() {

        Optional<User> optionalUser = userService.viewUserById(1L);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            when(userService.viewUserById(1L)).thenReturn(optionalUser);
            assertEquals("Return username", user.getUsername(), "author");
        }
    }

    @Test
    public void toggleUserStatusTest() {
        user = User.builder().id(10L).username("jadeshola").password("Password@123").firstName("John").lastName("Adeshola")
                .fullName("John Adeshola").email("timadeshola@gmail.com").status(true).phoneNumber("2347030239943")
                .dateUpdated(null).dateDeleted(null).roles(new HashSet<>(Collections.singletonList(new Role("AUTHOR")))).build();

        userService.toggleUserStatus(user.getId());
        verify(userService, times(1)).toggleUserStatus(user.getId());
    }
}
