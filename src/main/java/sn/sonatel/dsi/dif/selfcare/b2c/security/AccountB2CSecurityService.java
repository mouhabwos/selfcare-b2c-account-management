package sn.sonatel.dsi.dif.selfcare.b2c.security;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.security.oauth2.OAuth2TokenEndpointClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.ValidationHmacService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareUAAService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.FormatNumberPhoneUtil;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ResetPasswordVM;

@Service
public class AccountB2CSecurityService {

    private final Logger log = LoggerFactory.getLogger(AccountB2CSecurityService.class);
    private static final int PASSWORD_MAX_LENGTH = 19;


    private final AccountB2CService accountB2CService;
    private final ValidationHmacService validationHmacService;
    private final SelfcareUAAService uaaService;
    private final OAuth2TokenEndpointClient authorizationClient;


    public AccountB2CSecurityService(AccountB2CService accountB2CService, ValidationHmacService validationHmacService, OAuth2TokenEndpointClient authorizationClient, SelfcareUAAService uaaService) {
        this.accountB2CService = accountB2CService;
        this.validationHmacService = validationHmacService;
        this.uaaService = uaaService;
        this.authorizationClient = authorizationClient;
    }

    public OAuth2AccessToken registerAccountB2CV3(ManagedUserVM managedUserVM) {
        log.debug("Register account {}",managedUserVM);
        managedUserVM.setLogin(FormatNumberPhoneUtil.extractNumberWithoutSuffix(managedUserVM.getLogin()));
        validationHmacService.checkHmac(managedUserVM.getHmac(),managedUserVM.getLogin(), managedUserVM.getUuid());
        managedUserVM.setPassword(RandomStringUtils.random(PASSWORD_MAX_LENGTH,true,true));
        AccountB2C accountB2C = accountB2CService.register(managedUserVM, false);
        log.debug("Registered account {} ",accountB2C);
       return this.authorizationClient.sendPasswordGrant(managedUserVM.getLogin(), managedUserVM.getPassword());
    }

    public OAuth2AccessToken resetPassword(String uuid, ResetPasswordVM resetPasswordVM){
        log.debug("Service to reset password for account {}",resetPasswordVM);
        resetPasswordVM.setNewPassword(RandomStringUtils.random(PASSWORD_MAX_LENGTH,true,true));
        resetPasswordVM.setLogin(FormatNumberPhoneUtil.extractNumberWithoutSuffix(resetPasswordVM.getLogin()));
        ResponseEntity responseEntity = uaaService.resetPassword(resetPasswordVM, uuid);
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)){
            return this.authorizationClient.sendPasswordGrant(resetPasswordVM.getLogin(), resetPasswordVM.getNewPassword());
        }
        throw new HttpClientErrorException(responseEntity.getStatusCode());
    }

}
