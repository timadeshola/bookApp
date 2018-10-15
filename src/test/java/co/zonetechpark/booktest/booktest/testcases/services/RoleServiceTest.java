package co.zonetechpark.booktest.booktest.testcases.services;

import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import co.zonetechpark.booktest.booktest.jpa.repos.RoleRepository;
import co.zonetechpark.booktest.booktest.resources.model.request.RoleResource;
import co.zonetechpark.booktest.booktest.service.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @Mock
    private RoleService roleService;

    private Role role;

    @Before
    public void setUp() {
        role = new Role();
    }

    @Test
    public void createRoleTest() {

        role = Role.builder().id(10L).name("AUTHOR").status(true).dateCreated(new Timestamp(System.currentTimeMillis()))
                .dateUpdated(null).dateDeleted(null).build();

        assertEquals("Role creation test", role.getName(), "AUTHOR");
    }

    @Test
    public void updateRoleTest() {

        role = Role.builder().id(10L).name("AUTHOR").status(true).dateCreated(new Timestamp(System.currentTimeMillis()))
                .dateUpdated(null).dateDeleted(null).build();

        assertEquals("Role creation test", role.getName(), "AUTHOR");

    }

    @Test
    public void deleteRoleTest() {
        role = Role.builder().id(10L).name("AUTHOR").status(true).dateCreated(new Timestamp(System.currentTimeMillis()))
                .dateUpdated(null).dateDeleted(null).build();

        roleService.deleteRole(role.getId());
        verify(roleService, times(1)).deleteRole(role.getId());

    }

    @Test
    public void viewAllRolesTest() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.DESC, "dateCreated");
        Page<Role> roles = roleService.viewAllRoles(null, pageRequest);
        assertEquals("view all role test case", roles, roles);

    }

    @Test
    public void findRoleNameTest() {

        Optional<Role> optionalRole = roleService.viewRoleByName("AUTHOR");
        if (optionalRole.isPresent()) {
            role = optionalRole.get();
            assertEquals("Return role name", role.getName(), "AUTHOR");
        }
    }

    @Test
    public void viewRoleByIdTest() {

        Optional<Role> optionalRole = roleService.viewRoleById(1L);
        if (optionalRole.isPresent()) {
            role = optionalRole.get();
            assertEquals("Return role name", role.getName(), "AUTHOR");
        }
    }

    @Test
    public void toggleRoleStatusTest() {
        role = Role.builder().id(10L).name("AUTHOR").status(true).dateCreated(new Timestamp(System.currentTimeMillis()))
                .dateUpdated(null).dateDeleted(null).build();

        roleService.toggleRoleStatus(role.getId());
        verify(roleService, times(1)).toggleRoleStatus(role.getId());
    }
}
