package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.exception.AccountB2CException;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.LoginAttemptService;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;

@Qualifier(value = "LoginAttemptServiceImpl")
@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    @Qualifier("loadBalancedRestTemplate")
    private final RestTemplate restTemplate;

    private final AccountB2CRepository b2CRepository;

    private final ApplicationProperties applicationProperties;

    public LoginAttemptServiceImpl(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, AccountB2CRepository b2CRepository, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.b2CRepository = b2CRepository;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void loginSucceeded(String username) throws AccountB2CException {

        Optional<AccountB2C> accountB2C= b2CRepository.findOneByNumero(username);

        if (accountB2C.isPresent()){
            accountB2C.get().setAttempts(0);
            accountB2C.get().setDerniereConnnexionDate(ZonedDateTime.now());
            b2CRepository.save(accountB2C.get());
        }
    }

    @Override
    public void loginFailed(String username) throws AccountB2CException {

        Optional<AccountB2C> accountB2C= b2CRepository.findOneByNumero(username);


        if(accountB2C.isPresent() && accountB2C.get().getAttempts() < Constants.getMaxAttempts){

            if(accountB2C.get().getDerniereConnnexionDate() != null){
                boolean check = Duration.between (accountB2C.get().getDerniereConnnexionDate(), ZonedDateTime.now ()).getSeconds() < Constants.MAX_DELAY_TO_TRY_CONNEXION;

                if(check){
                    accountB2C.get().setAttempts(accountB2C.get().getAttempts()+1);
                    accountB2C.get().setDerniereConnnexionDate(ZonedDateTime.now());
                    b2CRepository.save(accountB2C.get());
                }else {

                    accountB2C.get().setAttempts(0);
                    accountB2C.get().setDerniereConnnexionDate(ZonedDateTime.now());
                    b2CRepository.save(accountB2C.get());
                }
            }else {

                accountB2C.get().setAttempts(accountB2C.get().getAttempts()+1);
                accountB2C.get().setDerniereConnnexionDate(ZonedDateTime.now());
                b2CRepository.save(accountB2C.get());

            }



        }
        if (isBlocked(username) ){
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            String url = applicationProperties.getAuthenticationAuthorisationUserServerHost() + "/api/disable?login="+username;
            restTemplate.getForEntity(url, Object.class);
        }
    }

    @Override
    public boolean isBlocked(String username) {

        Optional<AccountB2C> accountB2C= b2CRepository.findOneByNumero(username);

        return ( accountB2C.isPresent()) &&(accountB2C.get().getAttempts() >= Constants.getMaxAttempts);
    }
}
