package com.booktest.testcases.model;

import com.booktest.jpa.entity.Role;
import com.booktest.jpa.entity.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void testUser() {
        user = User.builder().id(10L).username("jadeshola").password("Password@123").firstName("John").lastName("Adeshola")
                .fullName("John Adeshola").email("timadeshola@gmail.com").status(true).phoneNumber("2347030239942")
                .dateUpdated(null).dateDeleted(null).roles(new HashSet<>(Collections.singletonList(new Role("AUTHOR")))).build();
        assertEquals("testing email", user.getEmail(), "timadeshola@gmail.com");
        assertEquals("testing phone", user.getPhoneNumber(), "2347030239942");
    }

    @Test
    public void testUsername() {
        user.setUsername("jadeshola");
        assertEquals("Test username", user.getUsername(), "jadeshola");
    }
}
