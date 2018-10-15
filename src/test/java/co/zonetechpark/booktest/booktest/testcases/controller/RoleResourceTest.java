package co.zonetechpark.booktest.booktest.testcases.controller;

import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.resources.controller.RoleController;
import co.zonetechpark.booktest.booktest.resources.model.request.RoleResource;
import co.zonetechpark.booktest.booktest.service.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(RoleController.class)
public class RoleResourceTest {

    @Mock
    private RoleService roleService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RoleController(roleService)).build();
    }

    @Test
    public void createRoleTest() throws Exception {
        RoleResource resource = RoleResource.builder().roleId(2L).name("AUTHOR").build();

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Role role = roleService.createRole(resource);
        if(role != null) {
            mockMvc
                    .perform(post("/api/v1/role/create")
                            .header("Authorization", auth)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andDo(MockMvcResultHandlers.print());
        }


    }

    @Test
    public void updateRoleTest() throws Exception {

        RoleResource resource = RoleResource.builder().roleId(1L).name("AUTHOR").build();

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Role role = roleService.createRole(resource);
        if(role != null) {
            mockMvc
                    .perform(put("/api/v1/role/update")
                            .header("Authorization", auth)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void viewAllRolesTest() throws Exception {

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Role> optionalRole= roleService.viewAllRoles(null, pageRequest);
        if(optionalRole != null) {

            when(roleService.viewAllRoles(null, pageRequest)).thenReturn(optionalRole);

            mockMvc.perform(get("/api/v1/role/all")
                    .header("Authorization", auth))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void deleteRoleTest() throws Exception {
        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        mockMvc.perform(delete("/api/v1/role/delete")
                .header("Authorization", auth)
                .param("roleId", "1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findRoleByNameTest() throws Exception {

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Optional<Role> optionalRole = roleService.viewRoleByName("AUTHOR");
        if(optionalRole.isPresent()) {

            when(roleService.viewRoleByName("AUTHOR")).thenReturn(optionalRole);

            mockMvc.perform(get("/api/v1/role/view-role-name")
                    .header("Authorization", auth)
            .param("name", "AUTHOR"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("name").value("AUTHOR"))
                .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void viewRoleByIdTest() throws Exception {
        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Optional<Role> optionalRole = roleService.viewRoleById(1L);
        if(optionalRole.isPresent()) {

            when(roleService.viewRoleById(1L)).thenReturn(optionalRole);

            mockMvc.perform(get("/api/v1/role/view-role")
                    .header("Authorization", auth)
                    .param("roleId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("name").value("AUTHOR"))
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    @Test
    public void toggleRoleStatusTest() throws Exception {

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        mockMvc.perform(put("/api/v1/role/status")
                .header("Authorization", auth)
                .param("roleId", "1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
