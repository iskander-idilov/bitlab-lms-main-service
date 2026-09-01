package kz.bitlab.springboot.mainservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
        String tokenUri,
        String clientId,
        String clientSecret
) {
}