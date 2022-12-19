package sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak-configuration.serverUrl}")
    private String serverUrl;

    @Value("${keycloak-configuration.realm}")
    private String realmName;

    @Value("${keycloak-configuration.client-id}")
    private String clientId;

    @Value("${keycloak-configuration.client-secret}")
    private String clientSecret;

    @Bean
    public KeycloakBuilder keycloakBuilder(){
        return KeycloakBuilder
            .builder()
            .serverUrl(serverUrl)
            .realm(realmName)
            .clientId(clientId)
            .clientSecret(clientSecret);
    }
}
