package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.config.KeycloakProperties;
import kz.bitlab.springboot.mainservice.dto.request.LoginRequest;
import kz.bitlab.springboot.mainservice.dto.request.RefreshTokenRequest;
import kz.bitlab.springboot.mainservice.dto.response.TokenResponse;
import kz.bitlab.springboot.mainservice.exception.InvalidCredentialsException;
import kz.bitlab.springboot.mainservice.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestClient keycloakRestClient;
    private final KeycloakProperties keycloakProperties;

    public TokenResponse login(LoginRequest request) {
        log.info("Authenticating user: {}", request.getUsername());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(Constants.Keycloak.GRANT_TYPE, Constants.Keycloak.GRANT_TYPE_PASSWORD);
        formData.add(Constants.Keycloak.CLIENT_ID, keycloakProperties.clientId());
        formData.add(Constants.Keycloak.CLIENT_SECRET, keycloakProperties.clientSecret());
        formData.add(Constants.Keycloak.USERNAME, request.getUsername());
        formData.add(Constants.Keycloak.PASSWORD, request.getPassword());

        try {
            return keycloakRestClient.post()
                    .uri(keycloakProperties.tokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(TokenResponse.class);
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.BadRequest e) {
            log.warn("Authentication failed for user: {} - Keycloak responded {}: {}",
                    request.getUsername(), e.getStatusCode(), e.getResponseBodyAsString());
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    public TokenResponse refresh(RefreshTokenRequest request) {
        log.info("Refreshing token");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(Constants.Keycloak.GRANT_TYPE, Constants.Keycloak.GRANT_TYPE_REFRESH_TOKEN);
        formData.add(Constants.Keycloak.CLIENT_ID, keycloakProperties.clientId());
        formData.add(Constants.Keycloak.CLIENT_SECRET, keycloakProperties.clientSecret());
        formData.add(Constants.Keycloak.REFRESH_TOKEN, request.refreshToken());

        try {
            return keycloakRestClient.post()
                    .uri(keycloakProperties.tokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .body(TokenResponse.class);
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.BadRequest e) {
            log.warn("Token refresh failed");
            throw new InvalidCredentialsException("Invalid or expired refresh token");
        }
    }
}
