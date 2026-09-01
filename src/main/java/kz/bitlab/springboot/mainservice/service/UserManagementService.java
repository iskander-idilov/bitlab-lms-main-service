package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.config.KeycloakProperties;
import kz.bitlab.springboot.mainservice.dto.request.CreateUserRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateRoleRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateUserRequest;
import kz.bitlab.springboot.mainservice.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final RestClient keycloakRestClient;
    private final KeycloakProperties keycloakProperties;

    public void createUser(CreateUserRequest request) {
        log.info("Creating user: username={}, email={}, role={}",
                request.username(), request.email(), request.role());

        String serviceToken = getServiceAccountToken();
        String userId = createKeycloakUser(request, serviceToken);
        setPassword(userId, request.password(), serviceToken);
        assignRole(userId, request.role().name(), serviceToken);

        log.info("User created successfully: {}", request.username());
    }

    private String getServiceAccountToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", keycloakProperties.clientId());
        formData.add("client_secret", keycloakProperties.clientSecret());

        Map<String, Object> response = keycloakRestClient.post()
                .uri(keycloakProperties.tokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(Map.class);

        return (String) response.get("access_token");
    }

    private String createKeycloakUser(CreateUserRequest request, String token) {
        Map<String, Object> body = Map.of(
                "username", request.username(),
                "email", request.email(),
                "firstName", request.firstName(),
                "lastName", request.lastName(),
                "enabled", true,
                "emailVerified", true
        );

        var response = keycloakRestClient.post()
                .uri(keycloakProperties.adminUsersUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();

        URI location = response.getHeaders().getLocation();
        if (location == null) {
            throw new InvalidCredentialsException("Failed to create user in Keycloak");
        }

        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private void setPassword(String userId, String password, String token) {
        Map<String, Object> body = Map.of(
                "type", "password",
                "value", password,
                "temporary", false
        );

        keycloakRestClient.put()
                .uri(keycloakProperties.adminUsersUri() + "/" + userId + "/reset-password")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    private void assignRole(String userId, String roleName, String token) {
        String realmRolesUri = keycloakProperties.adminUsersUri()
                .replace("/users", "/roles/" + roleName);

        Map<String, Object> role = keycloakRestClient.get()
                .uri(realmRolesUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(Map.class);

        keycloakRestClient.post()
                .uri(keycloakProperties.adminUsersUri() + "/" + userId + "/role-mappings/realm")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(List.of(role))
                .retrieve()
                .toBodilessEntity();
    }

    public void updateUser(String userId, UpdateUserRequest request) {
        log.info("Updating user: {}", userId);

        String serviceToken = getServiceAccountToken();

        if (request.firstName() != null || request.lastName() != null || request.email() != null) {
            updateKeycloakUser(userId, request, serviceToken);
        }

        if (request.password() != null) {
            setPassword(userId, request.password(), serviceToken);
        }

        log.info("User updated successfully: {}", userId);
    }

    public void updateUserRole(String userId, UpdateRoleRequest request) {
        log.info("Updating role for user: {} to {}", userId, request.role());

        String serviceToken = getServiceAccountToken();
        assignRole(userId, request.role().name(), serviceToken);

        log.info("Role updated successfully for user: {}", userId);
    }

    private void updateKeycloakUser(String userId, UpdateUserRequest request, String token) {
        Map<String, Object> body = new java.util.HashMap<>();
        if (request.firstName() != null) body.put("firstName", request.firstName());
        if (request.lastName() != null) body.put("lastName", request.lastName());
        if (request.email() != null) body.put("email", request.email());

        keycloakRestClient.put()
                .uri(keycloakProperties.adminUsersUri() + "/" + userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}