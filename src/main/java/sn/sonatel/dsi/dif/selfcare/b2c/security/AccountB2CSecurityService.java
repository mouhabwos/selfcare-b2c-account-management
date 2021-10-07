package sn.sonatel.dsi.dif.selfcare.b2c.security;

import io.github.jhipster.config.JHipsterProperties;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.ValidationHmacService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

@Service
public class AccountB2CSecurityService {

    private final Logger log = LoggerFactory.getLogger(AccountB2CSecurityService.class);
    private static final int PASSWORD_MAX_LENGTH = 19;


    protected final JHipsterProperties jHipsterProperties;
    protected final RestTemplate restTemplate;
    private final AccountB2CService accountB2CService;
    private final ValidationHmacService validationHmacService;


    public AccountB2CSecurityService(JHipsterProperties jHipsterProperties, @Qualifier("vanillaRestTemplate") RestTemplate restTemplate, AccountB2CService accountB2CService, ValidationHmacService validationHmacService) {
        this.jHipsterProperties = jHipsterProperties;
        this.restTemplate = restTemplate;
        this.accountB2CService = accountB2CService;
        this.validationHmacService = validationHmacService;
    }

    public OAuth2AccessToken registerAccountB2CV3(ManagedUserVM managedUserVM) {
        log.debug("Register account {}",managedUserVM);
        validationHmacService.checkHmac(managedUserVM.getHmac(),managedUserVM.getLogin(), managedUserVM.getUuid());
        managedUserVM.setPassword(RandomStringUtils.random(PASSWORD_MAX_LENGTH,true,true));
        AccountB2C accountB2C = accountB2CService.register(managedUserVM);
        log.debug("Registered account {}",accountB2C);
        return this.sendPasswordGrant(managedUserVM.getLogin(), managedUserVM.getPassword());
    }

    private OAuth2AccessToken sendPasswordGrant(String username, String password) {
        log.debug("authenticating account {}",username);
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.set("username", username);
        formParams.set("password", password);
        formParams.set("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formParams, reqHeaders);
        log.debug("contacting OAuth2 token endpoint to login user: {}", username);
        ResponseEntity<OAuth2AccessToken>
            responseEntity = restTemplate.postForEntity(getTokenEndpoint(), entity, OAuth2AccessToken.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            log.debug("failed to authenticate user with OAuth2 token endpoint, status: {}", responseEntity.getStatusCodeValue());
            throw new HttpClientErrorException(responseEntity.getStatusCode());
        }
        return responseEntity.getBody();
    }


    /**
     * Returns the configured OAuth2 token endpoint URI.
     *
     * @return the OAuth2 token endpoint URI.
     */
    private String getTokenEndpoint() {
        String tokenEndpointUrl = jHipsterProperties.getSecurity().getClientAuthorization().getAccessTokenUri();
        if (tokenEndpointUrl == null) {
            throw new InvalidClientException("no token endpoint configured in application properties");
        }
        return tokenEndpointUrl;
    }

}
