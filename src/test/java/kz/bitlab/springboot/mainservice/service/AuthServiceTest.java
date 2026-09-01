package kz.bitlab.springboot.mainservice.service;

import kz.bitlab.springboot.mainservice.config.KeycloakProperties;
import kz.bitlab.springboot.mainservice.dto.request.LoginRequest;
import kz.bitlab.springboot.mainservice.dto.request.RefreshTokenRequest;
import kz.bitlab.springboot.mainservice.dto.response.TokenResponse;
import kz.bitlab.springboot.mainservice.exception.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldReturnTokenResponseWhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin1");
        request.setPassword("correctPassword");

        TokenResponse expectedResponse = new TokenResponse();
        expectedResponse.setAccessToken("access-token");
        expectedResponse.setRefreshToken("refresh-token");

        when(keycloakProperties.clientId()).thenReturn("main-service");
        when(keycloakProperties.clientSecret()).thenReturn("secret");
        when(keycloakProperties.tokenUri()).thenReturn("http://keycloak/token");

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TokenResponse.class)).thenReturn(expectedResponse);

        TokenResponse result = authService.login(request);

        assertEquals("access-token", result.getAccessToken());
        assertEquals("refresh-token", result.getRefreshToken());
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenKeycloakRejectsLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin1");
        request.setPassword("wrongPassword");

        when(keycloakProperties.clientId()).thenReturn("main-service");
        when(keycloakProperties.clientSecret()).thenReturn("secret");
        when(keycloakProperties.tokenUri()).thenReturn("http://keycloak/token");

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TokenResponse.class))
                .thenThrow(HttpClientErrorException.create(
                        HttpStatusCode.valueOf(401), "Unauthorized", null, null, null));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void shouldReturnTokenResponseWhenRefreshTokenIsValid() {
        RefreshTokenRequest request = new RefreshTokenRequest("valid-refresh-token");

        TokenResponse expectedResponse = new TokenResponse();
        expectedResponse.setAccessToken("new-access-token");
        expectedResponse.setRefreshToken("new-refresh-token");

        when(keycloakProperties.clientId()).thenReturn("main-service");
        when(keycloakProperties.clientSecret()).thenReturn("secret");
        when(keycloakProperties.tokenUri()).thenReturn("http://keycloak/token");

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TokenResponse.class)).thenReturn(expectedResponse);

        TokenResponse result = authService.refresh(request);

        assertEquals("new-access-token", result.getAccessToken());
    }

    @Test
    void shouldThrowInvalidCredentialsExceptionWhenRefreshTokenIsInvalid() {
        RefreshTokenRequest request = new RefreshTokenRequest("invalid-refresh-token");

        when(keycloakProperties.clientId()).thenReturn("main-service");
        when(keycloakProperties.clientSecret()).thenReturn("secret");
        when(keycloakProperties.tokenUri()).thenReturn("http://keycloak/token");

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TokenResponse.class))
                .thenThrow(HttpClientErrorException.create(
                        HttpStatusCode.valueOf(401), "Unauthorized", null, null, null));

        assertThrows(InvalidCredentialsException.class, () -> authService.refresh(request));
    }
}