package kz.bitlab.springboot.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.springboot.mainservice.config.SecurityConfig;
import kz.bitlab.springboot.mainservice.dto.request.LoginRequest;
import kz.bitlab.springboot.mainservice.dto.response.TokenResponse;
import kz.bitlab.springboot.mainservice.exception.InvalidCredentialsException;
import kz.bitlab.springboot.mainservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @Test
    void shouldReturnTokenWhenLoginSucceeds() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin1");
        request.setPassword("correctPassword");

        TokenResponse response = new TokenResponse();
        response.setAccessToken("access-token");
        response.setRefreshToken("refresh-token");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("access-token"));
    }

    @Test
    void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin1");
        request.setPassword("wrongPassword");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnBadRequestWhenUsernameIsBlank() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("password");

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}