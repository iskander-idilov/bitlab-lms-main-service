package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.config.KeycloakProperties;
import kz.bitlab.springboot.mainservice.dto.request.LoginRequest;
import kz.bitlab.springboot.mainservice.dto.response.TokenResponse;
import kz.bitlab.springboot.mainservice.exception.InvalidCredentialsException;
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
        formData.add("grant_type", "password");
        formData.add("client_id", keycloakProperties.clientId());
        formData.add("client_secret", keycloakProperties.clientSecret());
        formData.add("username", request.getUsername());
        formData.add("password", request.getPassword());

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
}
