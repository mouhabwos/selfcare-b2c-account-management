package sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;

@Service
public class ApiManagementAccessTokenRetrieveService {

    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;

    public ApiManagementAccessTokenRetrieveService(@Qualifier("vanillaRestTemplate") RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    String retrieveToken(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", applicationProperties.getApiManagement().getOauth2().getGrantType());
        map.add("client_id", applicationProperties.getApiManagement().getOauth2().getClientId());
        map.add("client_secret", applicationProperties.getApiManagement().getOauth2().getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<AuthenticationToken> response = restTemplate.postForEntity(applicationProperties.getApiManagement().getOauth2().getClientTokenUri(), request, AuthenticationToken.class);

        return response.getBody().accessToken;

    }


    @Getter
    @Setter
    @NoArgsConstructor
    private static class AuthenticationToken {

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private Long expiresIn;

        @JsonProperty("refresh_expires_in")
        private Long refreshExpiresIn;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("not_before_policy")
        private String notBeforePolicy;

        @JsonProperty("session_state")
        private String sessionState;

        @JsonProperty("scope")
        private String scope;

    }
}
