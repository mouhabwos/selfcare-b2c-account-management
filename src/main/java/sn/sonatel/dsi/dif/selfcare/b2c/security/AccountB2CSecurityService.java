package sn.sonatel.dsi.dif.selfcare.b2c.security;

import org.apache.commons.lang.RandomStringUtils;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.ValidationHmacService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak.KeycloakServices;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak.dto.UserCredentialDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ResetPasswordVM;

@Service
public class AccountB2CSecurityService {

    private final Logger log = LoggerFactory.getLogger(AccountB2CSecurityService.class);
    private static final int PASSWORD_MAX_LENGTH = 19;

    private final AccountB2CService accountB2CService;
    private final ValidationHmacService validationHmacService;
    private final KeycloakServices keycloakServices;


    public AccountB2CSecurityService(AccountB2CService accountB2CService, ValidationHmacService validationHmacService, KeycloakServices keycloakServices) {
        this.accountB2CService = accountB2CService;
        this.validationHmacService = validationHmacService;
        this.keycloakServices = keycloakServices;
    }

    public AccessTokenResponse registerAccountB2CV3(ManagedUserVM managedUserVM) {
        log.debug("Register account {}",managedUserVM);
        managedUserVM.setLogin(FormatNumberPhoneUtil.extractNumberWithoutSuffix(managedUserVM.getLogin()));
        validationHmacService.checkHmac(managedUserVM.getHmac(),managedUserVM.getLogin(), managedUserVM.getUuid());
        managedUserVM.setPassword(RandomStringUtils.random(PASSWORD_MAX_LENGTH,true,true));
        AccountB2C accountB2C = accountB2CService.register(managedUserVM, false);
        log.debug("Registered account {} ",accountB2C);
        UserCredentialDTO userCredential = new UserCredentialDTO();
        userCredential.setUsername(managedUserVM.getLogin());
        userCredential.setPassword(managedUserVM.getPassword());
        ResponseEntity<AccessTokenResponse> responseEntity = keycloakServices.getToken(userCredential);
        if(responseEntity.getStatusCode().equals(HttpStatus.OK) && responseEntity.getBody()!=null){
            return responseEntity.getBody();
        }
        throw new HttpClientErrorException(responseEntity.getStatusCode());
    }

    public AccessTokenResponse resetPassword(String uuid, ResetPasswordVM resetPasswordVM){
        log.debug("Service to reset password for account {}",resetPasswordVM);
        validationHmacService.checkHmac(resetPasswordVM.getHmac(),resetPasswordVM.getLogin(), uuid);
        resetPasswordVM.setNewPassword(RandomStringUtils.random(PASSWORD_MAX_LENGTH,true,true));
        resetPasswordVM.setLogin(FormatNumberPhoneUtil.extractNumberWithoutSuffix(resetPasswordVM.getLogin()));
        ResponseEntity responseEntity = keycloakServices.resetPassword(resetPasswordVM);

        if(responseEntity.getStatusCode().equals(HttpStatus.ACCEPTED) || responseEntity.getStatusCode().equals(HttpStatus.CREATED)){
            UserCredentialDTO userCredential = new UserCredentialDTO();
            userCredential.setUsername(resetPasswordVM.getLogin());
            userCredential.setPassword(resetPasswordVM.getNewPassword());
            ResponseEntity<AccessTokenResponse> keycloakResponse = keycloakServices.getToken(userCredential);
            if(keycloakResponse.getStatusCode().equals(HttpStatus.OK) && keycloakResponse.getBody()!=null){
                return keycloakResponse.getBody();
            }
            throw new HttpClientErrorException(responseEntity.getStatusCode());
        }

        throw new HttpClientErrorException(responseEntity.getStatusCode());
    }

}
