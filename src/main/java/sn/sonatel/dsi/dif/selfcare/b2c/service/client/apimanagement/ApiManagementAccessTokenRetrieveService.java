package sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.service.impl.AccountB2CServiceImpl;

@Service
public class ApiManagementAccessTokenRetrieveService {

    private final RestTemplate restTemplate;
    private final ApplicationProperties applicationProperties;

    private final Logger log = LoggerFactory.getLogger(ApiManagementAccessTokenRetrieveService.class);


    public ApiManagementAccessTokenRetrieveService(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    String retrieveToken(){

        ApplicationProperties.ApiManagement.Oauth2 oauth2 =this.applicationProperties.getApiManagement().getOauth2();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        log.info("Get api token from Tiers {} {} {} {}",oauth2.getClientTokenUri(),oauth2.getGrantType(),oauth2.getClientId(),oauth2.getClientSecret());

        ResponseEntity<AuthenticationToken> response = restTemplate.exchange(oauth2.getClientTokenUri(),
            HttpMethod.GET,
            entity,
            AuthenticationToken.class,
            oauth2.getGrantType(),
            oauth2.getClientId(),
            oauth2.getClientSecret()

        );
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
