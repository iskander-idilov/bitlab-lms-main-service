package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.config.KeycloakProperties;
import kz.bitlab.springboot.mainservice.dto.request.CreateUserRequest;
import kz.bitlab.springboot.mainservice.dto.request.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import java.net.URI;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagementServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private UserManagementService userManagementService;

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserRequest request = new CreateUserRequest(
                "newuser", "new@example.com", "New", "User", "password123", UserRole.ROLE_TEACHER
        );

        when(keycloakProperties.clientId()).thenReturn("main-service");
        when(keycloakProperties.clientSecret()).thenReturn("secret");
        when(keycloakProperties.tokenUri()).thenReturn("http://keycloak/token");
        when(keycloakProperties.adminUsersUri()).thenReturn("http://keycloak/admin/realms/bitlab-lms/users");

        Map<String, Object> tokenResponse = Map.of("access_token", "service-token");
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Map.class)).thenReturn(tokenResponse);

        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        ResponseEntity<Void> createdResponse = ResponseEntity.created(
                URI.create("http://keycloak/admin/realms/bitlab-lms/users/user-id-123")).build();
        when(responseSpec.toBodilessEntity()).thenReturn(createdResponse);

        Map<String, Object> roleResponse = Map.of("id", "role-id", "name", "ROLE_TEACHER");
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(Map.class)).thenReturn(roleResponse);

        when(restClient.put()).thenReturn(requestBodyUriSpec);

        assertDoesNotThrow(() -> userManagementService.createUser(request));

        verify(restClient, atLeastOnce()).post();
    }
}