package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.LoginAttemptService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.NotificationInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SMSNotificationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

@Qualifier(value = "LoginAttemptServiceImpl")
@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final Logger log = LoggerFactory.getLogger(LoginAttemptServiceImpl.class);


    private final AccountB2CRepository b2CRepository;

    private final ApplicationProperties applicationProperties;

    private final SMSNotificationService smsNotificationService;

    private final NotificationInformationService notificationInformationService;

    public LoginAttemptServiceImpl(AccountB2CRepository b2CRepository, ApplicationProperties applicationProperties, SMSNotificationService smsNotificationService, NotificationInformationService notificationInformationService) {
        this.b2CRepository = b2CRepository;
        this.applicationProperties = applicationProperties;
        this.smsNotificationService = smsNotificationService;
        this.notificationInformationService = notificationInformationService;
    }

    @Override
    public void loginSucceeded(String username){

        log.debug("Service Request Authentication Success " );
        Optional<AccountB2C> accountB2C= b2CRepository.findOneByNumero(username);

        if (accountB2C.isPresent()){
            accountB2C.get().setAttempts(0);
            accountB2C.get().setDerniereConnnexionDate(ZonedDateTime.now());
            b2CRepository.save(accountB2C.get());
            updateCodeFormule(accountB2C.get().getNumero());
        }
    }

    @Override
    public int loginFailed(String username) {

        log.debug("Service Request Authentication Failure" );
        Optional<AccountB2C> accountB2C= b2CRepository.findOneByNumero(username);
        int attemps = 2;

        if(!accountB2C.isPresent()){
            return -1;
        }
        if(accountB2C.get().getAttempts() < 3){

            if(accountB2C.get().getDerniereConnnexionDate() != null){
                boolean check = Duration.between (accountB2C.get().getDerniereConnnexionDate(), ZonedDateTime.now ()).getSeconds() < Constants.MAX_DELAY_TO_TRY_CONNEXION;

                if(check){
                    accountB2C.get().setAttempts(accountB2C.get().getAttempts()+1);
                    accountB2C.get().setDerniereConnnexionDate(ZonedDateTime.now());
                    b2CRepository.save(accountB2C.get());
                    attemps = applicationProperties.getMaxAttempts() - accountB2C.get().getAttempts();
                }else {

                    accountB2C.get().setAttempts(0);
                    accountB2C.get().setDerniereConnnexionDate(ZonedDateTime.now());
                    b2CRepository.save(accountB2C.get());
                }
            }else {

                accountB2C.get().setAttempts(accountB2C.get().getAttempts()+1);
                accountB2C.get().setDerniereConnnexionDate(ZonedDateTime.now());
                b2CRepository.save(accountB2C.get());
                attemps = applicationProperties.getMaxAttempts() - accountB2C.get().getAttempts();

            }

        }
       if (isBlocked(username) ){
            MessageVM messageVM = new MessageVM();
            messageVM.setMessage(applicationProperties.getMessageBlockUser()+applicationProperties.getServiceClientOrange()+applicationProperties.getLienIbou());
            messageVM.setMsisdn(username);
            smsNotificationService.sendSMSPP(messageVM.getMsisdn(),messageVM.getMessage(),messageVM.getSourceAddress());
           return 0;
       }
        return attemps;
    }

    @Override
    public boolean isBlocked(String username) {

        Optional<AccountB2C> accountB2C= b2CRepository.findOneByNumero(username);

        return ( accountB2C.isPresent()) &&(accountB2C.get().getAttempts() >= applicationProperties.getMaxAttempts());
    }

    @Async
    void updateCodeFormule(String msisdn){
        notificationInformationService.updateNotificationInformationFormulCode(msisdn);
    }
}
