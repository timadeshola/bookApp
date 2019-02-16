package com.booktest.testcases.repository;

import com.booktest.jpa.entity.Role;
import com.booktest.jpa.repos.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @Before
    public void setUp() {
        role = new Role();
    }
    @Test
    public void createRoleTest() {
        role = Role.builder().id(1L).name("AUTHOR").dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).build();
        role = roleRepository.saveAndFlush(role);
        assertThat(role.getName()).isEqualTo("AUTHOR");
    }

    @Test
    public void updateRoleTest() {
        role = Role.builder().id(1L).name("AUTHOR").dateCreated(new Timestamp(System.currentTimeMillis())).dateUpdated(null).dateDeleted(null).build();
        role = roleRepository.save(role);
        assertThat(role.getName()).isEqualTo("AUTHOR");
    }

    @Test
    public void viewRoleByName() {
        Optional<Role> optionalRole = roleRepository.findRoleByName("AUTHOR");
        if(optionalRole.isPresent()) {
            role = optionalRole.get();
            assertThat(role.getName()).isEqualTo("AUTHOR");
            assertThat(role.getName()).isNotNull();
        }
    }

    @Test
    public void viewRoleById() {
        Optional<Role> optionalRole = roleRepository.findById(1L);
        if(optionalRole.isPresent()) {
            role = optionalRole.get();
            assertThat(role.getName()).isEqualTo("AUTHOR");
            assertThat(role.getName()).isNotNull();
            assertThat(role.getName()).isIn(role);
        }
    }

    @Test
    public void deleteRoleById() {
        Optional<Role> optionalRole = roleRepository.findById(1L);
        if(optionalRole.isPresent()) {
            role = optionalRole.get();
            roleRepository.deleteById(role.getId());
            assertThat(role.getName()).isNotIn(role);
        }
    }
}
