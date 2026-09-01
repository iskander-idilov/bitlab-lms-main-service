package kz.bitlab.springboot.mainservice.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.springboot.mainservice.config.SecurityConfig;
import kz.bitlab.springboot.mainservice.dto.request.CreateUserRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateRoleRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateUserRequest;
import kz.bitlab.springboot.mainservice.dto.request.UserRole;
import kz.bitlab.springboot.mainservice.service.UserManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserManagementService userManagementService;

    @Test
    void shouldCreateUserWhenAdminRole() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "newuser", "new@example.com", "New", "User", "password123", UserRole.ROLE_TEACHER
        );

        doNothing().when(userManagementService).createUser(any(CreateUserRequest.class));

        mockMvc.perform(post("/users")
                        .with(jwt().authorities(() -> "ROLE_ADMIN"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnForbiddenWhenNotAdmin() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "newuser", "new@example.com", "New", "User", "password123", UserRole.ROLE_TEACHER
        );

        mockMvc.perform(post("/users")
                        .with(jwt().authorities(() -> "ROLE_STUDENT"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldUpdateOwnDataWhenAuthenticated() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("NewName", null, null, null);

        doNothing().when(userManagementService).updateUser(any(String.class), any(UpdateUserRequest.class));

        mockMvc.perform(patch("/users/me")
                        .with(jwt().jwt(jwt -> jwt.subject("user-id-123")))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateRoleWhenAdmin() throws Exception {
        UpdateRoleRequest request = new UpdateRoleRequest(UserRole.ROLE_TEACHER);

        doNothing().when(userManagementService).updateUserRole(any(String.class), any(UpdateRoleRequest.class));

        mockMvc.perform(patch("/users/some-id/role")
                        .with(jwt().authorities(() -> "ROLE_ADMIN"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnForbiddenWhenUpdateRoleWithoutAdmin() throws Exception {
        UpdateRoleRequest request = new UpdateRoleRequest(UserRole.ROLE_TEACHER);

        mockMvc.perform(patch("/users/some-id/role")
                        .with(jwt().authorities(() -> "ROLE_STUDENT"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}