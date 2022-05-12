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
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareOTPService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneAlreadyRattachedException;
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
    private final SelfcareOTPService selfcareOTPService;


    public AccountB2CSecurityService(AccountB2CService accountB2CService, ValidationHmacService validationHmacService, KeycloakServices keycloakServices, SelfcareOTPService selfcareOTPService) {
        this.accountB2CService = accountB2CService;
        this.validationHmacService = validationHmacService;
        this.keycloakServices = keycloakServices;
        this.selfcareOTPService = selfcareOTPService;
    }

    public AccessTokenResponse registerAccountB2CV3(ManagedUserVM managedUserVM) {
        log.debug("Register account {}",managedUserVM);
        String login = FormatNumberPhoneUtil.extractNumberWithoutSuffix(managedUserVM.getLogin());
        validationHmacService.checkHmac(managedUserVM.getHmac(),login, managedUserVM.getUuid());
        return registerUserWithGeneratedPasswprd(login);
    }

    public AccessTokenResponse resetPassword(String uuid, ResetPasswordVM resetPasswordVM){
        log.debug("Service to reset password for account {}",resetPasswordVM);
        validationHmacService.checkHmac(resetPasswordVM.getHmac(),resetPasswordVM.getLogin(), uuid);
        return resetPasswordWithGeneratedValueAndRetrieveToken(resetPasswordVM.getLogin());
    }

    public AccessTokenResponse loginWithOtpCode(String login, String password) {
        log.debug("Login account {} with otp {}",login,password);

        if (!selfcareOTPService.checkOPT(login,password).isValid()){
            throw new BadRequestAlertException("Le code otp n'est pas valide, veuillez essayer à nouveau ","RattachementLigne","notValidCode");
        }

        if ( accountB2CService.isLinkedAccount(login)) {
            log.debug("Login account {} is a linked account unable to process",login);
            throw new LigneAlreadyRattachedException();
        }
        if (accountB2CService.isPrincipalAccount(login)) {
            log.debug("Login account {} is a principal account processing with login",login);
            return resetPasswordWithGeneratedValueAndRetrieveToken(login);
        }

        return registerUserWithGeneratedPasswprd(login);

    }

    private AccessTokenResponse registerUserWithGeneratedPasswprd(String login) {
        log.debug("Register account {}", login);

        var managedUserVM = new ManagedUserVM();
        managedUserVM.setLogin(FormatNumberPhoneUtil.extractNumberWithoutSuffix(login));
        managedUserVM.setPassword(RandomStringUtils.random(PASSWORD_MAX_LENGTH,true,true));
        AccountB2C accountB2C = accountB2CService.register(managedUserVM, false);
        log.debug("Registered account {} ",accountB2C);

        var userCredential = new UserCredentialDTO();
        userCredential.setUsername(managedUserVM.getLogin());
        userCredential.setPassword(managedUserVM.getPassword());
        ResponseEntity<AccessTokenResponse> responseEntity = keycloakServices.getToken(userCredential);
        if(responseEntity.getStatusCode().equals(HttpStatus.OK) && responseEntity.getBody()!=null){
            return responseEntity.getBody();
        }
        throw new HttpClientErrorException(responseEntity.getStatusCode());
    }

    private AccessTokenResponse resetPasswordWithGeneratedValueAndRetrieveToken(String login) {
        var resetPasswordVM= new ResetPasswordVM();
        resetPasswordVM.setNewPassword(RandomStringUtils.random(PASSWORD_MAX_LENGTH,true,true));
        resetPasswordVM.setLogin(FormatNumberPhoneUtil.extractNumberWithoutSuffix(login));
        var responseEntity = keycloakServices.resetPassword(resetPasswordVM, null);

        if(responseEntity.getStatusCode().equals(HttpStatus.ACCEPTED)){
            var userCredential = new UserCredentialDTO();
            userCredential.setUsername(resetPasswordVM.getLogin());
            userCredential.setPassword(resetPasswordVM.getNewPassword());
            ResponseEntity<AccessTokenResponse> keycloakResponse = keycloakServices.getToken(userCredential);
            if(keycloakResponse.getStatusCode().equals(HttpStatus.OK) && keycloakResponse.getBody()!=null){
                return keycloakResponse.getBody();
            }
            throw new HttpClientErrorException(keycloakResponse.getStatusCode());
        }

        throw new HttpClientErrorException(responseEntity.getStatusCode());
    }

}
