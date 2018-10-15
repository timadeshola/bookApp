package co.zonetechpark.booktest.booktest.testcases.model;

import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

public class RoleTest {

    private Role role;

    @Before
    public void setUp() {
        role = new Role();
    }

    @Test
    public void testRole() {
        role = Role.builder().id(10L).name("AUTHOR").status(true).dateCreated(new Timestamp(System.currentTimeMillis()))
                .dateUpdated(null).dateDeleted(null).build();
        assertEquals("Test role name", role.getName(), "AUTHOR");
        assertEquals("Test role status", role.getStatus(), true);
    }

    @Test
    public void testRoleName() {
        role.setName("EDITOR");
        assertEquals("Role name test case", role.getName(), "EDITOR");
    }
}
