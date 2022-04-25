package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.BoosterClient;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.BoosterTrigger;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;

@Service
public class AsyncService {
    private final Logger log = LoggerFactory.getLogger(AsyncService.class);

    private final BoosterClient boosterClient;

    private final NotificationInformationService notificationInformationService;

    private final AbonneService abonneService;

    private final AccountB2CRepository accountB2CRepository;

    public AsyncService(BoosterClient boosterClient, NotificationInformationService notificationInformationService, AbonneService abonneService, AccountB2CRepository accountB2CRepository) {
        this.boosterClient = boosterClient;
        this.notificationInformationService = notificationInformationService;
        this.abonneService = abonneService;
        this.accountB2CRepository = accountB2CRepository;
    }


    @Async
    public void applyWelcomeBooster(String msisdn) {
        log.info("Request to apply welcome booster for client {} ", msisdn);
        this.boosterClient.applyBooster(msisdn,null, BoosterTrigger.FORM_INSCRIPTION.name(),null);
    }

    @Async
    public void addCodeFormuleInformationNotification(String msisdn){
        notificationInformationService.addCodeFormuleCustomerOffer(msisdn);
    }

    @Async
    public void updateFirstnameAndLasname(AccountB2C account){
        log.debug(" Update user information for Account : {}", account);
        if(account.getFirstName().equals("") && account.getLastName().equals("")){
            AbonneDTO informationAbonne = abonneService.getInformationAbonne(account.getNumero());

            if(!informationAbonne.getNomAbonne().equals("")){
                account.setLastName(informationAbonne.getNomAbonne());
                account = accountB2CRepository.save(account);
            }
            if(!informationAbonne.getPrenomAbonne().equals("")){
                account.setFirstName(informationAbonne.getPrenomAbonne());
                accountB2CRepository.save(account);
            }
        }
    }
}
