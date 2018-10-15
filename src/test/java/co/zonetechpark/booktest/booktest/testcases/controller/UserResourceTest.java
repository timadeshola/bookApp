package co.zonetechpark.booktest.booktest.testcases.controller;

import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.resources.controller.UserController;
import co.zonetechpark.booktest.booktest.resources.model.request.UserResource;
import co.zonetechpark.booktest.booktest.service.UserService;
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

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(UserController.class)
public class UserResourceTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @Test
    public void createUserTest() throws Exception {
        UserResource resource = UserResource.builder().username("author").password("Password@123").firstName("Peter")
                .lastName("Verhas").email("author@example.com").phoneNumber("+2347012345675")
                .roleIds(Collections.singletonList(1L)).build();

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        User user = userService.createUser(resource);
        if(user != null) {
            mockMvc
                    .perform(post("/api/v1/user/create")
                            .header("Authorization", auth)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void updateUserTest() throws Exception {
        UserResource resource = UserResource.builder().id(1L).username("author").password("Password@123").firstName("Peter")
                .lastName("Verhas").email("author@example.com").phoneNumber("+2347012345675")
                .roleIds(Collections.singletonList(1L)).build();

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        User user = userService.updateUser(resource);
        if(user != null) {
            mockMvc
                    .perform(post("/api/v1/user/update")
                            .header("Authorization", auth)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void deleteUserTest() throws Exception {
        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        mockMvc.perform(delete("/api/v1/user/delete")
                .header("Authorization", auth)
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void viewAllUsersTest() throws Exception {

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<User> optionalUser = userService.viewAllUsers(null, pageRequest);
        if(optionalUser != null) {

            when(userService.viewAllUsers(null, pageRequest)).thenReturn(optionalUser);

            mockMvc.perform(get("/api/v1/user/all")
                    .header("Authorization", auth))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void findUserByUsernameTest() throws Exception {

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Optional<User> optionalUser = userService.viewUserByUsername("author");
        if(optionalUser.isPresent()) {

            when(userService.viewUserByUsername("author")).thenReturn(optionalUser);

            mockMvc.perform(get("/api/v1/user/view-user-username")
                    .header("Authorization", auth)
            .param("username", "author"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("username").value("author"))
                    .andExpect(jsonPath("firstName").value("Peter"))
                .andDo(MockMvcResultHandlers.print());
        }

    }

    @Test
    public void viewUserByIdTest() throws Exception {
        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        Optional<User> optionalUser = userService.viewUserById(1L);
        if(optionalUser.isPresent()) {

            when(userService.viewUserById(1L)).thenReturn(optionalUser);

            mockMvc.perform(get("/api/v1/user/view-user")
                    .header("Authorization", auth)
                    .param("userId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("username").value("author"))
                    .andExpect(jsonPath("firstName").value("Peter"))
                    .andDo(MockMvcResultHandlers.print());
        }
    }

    @Test
    public void toggleUserStatusTest() throws Exception {

        String auth = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRob3IiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJBVVRIT1IifSx7ImF1dGhvcml0eSI6IlBVQkxJU0hFUiJ9LHsiYXV0aG9yaXR5IjoiUkVWSUVXRVIifV0sImlhdCI6MTUzOTU2MDk0NiwiZXhwIjoxNTM5NTY0NTQ2fQ.5uIWlMY5lXssne2FAJMhN1z_bDDnoc4yxIwtDtrZomzq-b5fopoo0WHWKBP8EiHp1Cx4GCN8a-9JKm6EShb8Fg";

        mockMvc.perform(put("/api/v1/user/status")
                .header("Authorization", auth)
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
