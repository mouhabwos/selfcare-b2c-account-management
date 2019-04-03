package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.exception.AccountB2CException;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.LoginAttemptService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.SelfcareUAAService;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@Qualifier(value = "LoginAttemptServiceImpl")
@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {



    @Qualifier("loadBalancedRestTemplate")
    private final RestTemplate restTemplate;

    private final AccountB2CRepository b2CRepository;

    private final MailService mailService;

    private final ApplicationProperties applicationProperties;

    private final SelfcareUAAService selfcareUAAService;

    public LoginAttemptServiceImpl(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, AccountB2CRepository b2CRepository, MailService mailService, ApplicationProperties applicationProperties, SelfcareUAAService selfcareUAAService) {
        this.restTemplate = restTemplate;
        this.b2CRepository = b2CRepository;
        this.mailService = mailService;
        this.applicationProperties = applicationProperties;
        this.selfcareUAAService = selfcareUAAService;
    }

    @Override
    public void loginSucceeded(String username) throws AccountB2CException {

        Optional<AccountB2C> accountB2C= b2CRepository.findOneByNumero(username);

        if (accountB2C.isPresent()){
            accountB2C.get().setAttempts(0);
            accountB2C.get().setDerniereConnnexionDate(Instant.now());
            b2CRepository.save(accountB2C.get());
        }
    }

    @Override
    public void loginFailed(String username) throws AccountB2CException {

        Optional<AccountB2C> accountB2C= b2CRepository.findOneByNumero(username);

        if(accountB2C.isPresent() && accountB2C.get().getAttempts() < applicationProperties.getMaxAttempts()){
            accountB2C.get().setAttempts(accountB2C.get().getAttempts()+1);
            b2CRepository.save(accountB2C.get());

        }
        if (isBlocked(username) ){
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            String url = applicationProperties.getAuthenticationAuthorisationUserServerHost() + "/api/disable?login="+username;
            restTemplate.getForEntity(url, ResponseEntity.class);

           /* selfcareUAAService.disableUser(username);*/
        }

    }

    @Override
    public boolean isBlocked(String username) {
        Optional<AccountB2C> accountB2C= b2CRepository.findOneByNumero(username);
        return ( accountB2C.isPresent()) &&(accountB2C.get().getAttempts() >= applicationProperties.getMaxAttempts())   ;
    }
}
