package com.booktest.testcases.repository;

import com.booktest.jpa.entity.Role;
import com.booktest.jpa.entity.User;
import com.booktest.jpa.repos.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
    }
    @Test
    public void createUserTest() {
        Role role = Role.builder().id(1L).name("AUTHOR").dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).build();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        user = User.builder().id(10L).username("jadeshola").password("Password@123").firstName("John").lastName("Adeshola")
                .fullName("John Adeshola").email("timadeshola@gmail.com").status(true).phoneNumber("2347030239942")
                .dateUpdated(null).dateDeleted(null).roles(roleSet).build();
        userRepository.save(user);
        assertThat(user.getUsername()).isEqualTo("jadeshola");
    }

    @Test
    public void updateUserTest() {
        Role role = Role.builder().id(1L).name("AUTHOR").dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).build();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        user = User.builder().id(10L).username("jadeshola").password("Password@123").firstName("John").lastName("Adeshola1")
                .fullName("John Adeshola").email("timadeshola@gmail.com").status(true).phoneNumber("2347030239942")
                .dateUpdated(null).dateDeleted(null).roles(roleSet).build();
        userRepository.save(user);
        assertThat(user.getUsername()).isEqualTo("jadeshola");
    }

    @Test
    public void viewUserByUsername() {
        Optional<User> optionalUser = userRepository.findUserByUsername("jadeshola");
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
            assertThat(user.getUsername()).isEqualTo("jadeshola");
            assertThat(user.getUsername()).isNotNull();
            assertThat(user.getUsername()).isIn(user);
        }
    }

    @Test
    public void viewUserById() {
        Optional<User> optionalUser = userRepository.findById(1L);
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
            assertThat(user.getUsername()).isEqualTo("author");
            assertThat(user.getUsername()).isNotNull();
            assertThat(user.getUsername()).isIn(user);
        }
    }

    @Test
    public void deleteUserById() {
        Optional<User> optionalUser = userRepository.findById(1L);
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
            userRepository.deleteById(user.getId());
            assertThat(user.getUsername()).isNotIn(user);
        }
    }
}
