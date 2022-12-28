package sn.sonatel.dsi.dif.selfcare.b2c.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.NotificationInformationRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak.KeycloakServices;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeleteAccountService {
    private final Logger log = LoggerFactory.getLogger(DeleteAccountService.class);

    private final AccountB2CRepository accountB2CRepository;
    private final NotificationInformationRepository notificationInformationRepository;
    private final KeycloakServices keycloakServices;
    private final RattachementLigneRepository rattachementLigneRepository;
    private final SMSNotificationService smsNotificationService;
    private final ApplicationProperties applicationProperties;


    public void purgeNumberInfos(String msisdn){
        log.debug("Purge number {} informations",msisdn);

        deleteRattachementLigne(msisdn);

        deletePrincipaAccount(msisdn);

        keycloakServices.resetUser(msisdn);

    }

    private void deletePrincipaAccount(String msisdn) {
        Optional<AccountB2C> numeroPrincipal = accountB2CRepository.findOneByNumero(msisdn);
        numeroPrincipal.ifPresent(accountB2C -> {
            this.deleteLigneRattacheForPrincipal(accountB2C);

            var notifInfos = notificationInformationRepository.findAllByAccountB2CNumeroIn(Arrays.asList(msisdn));
            notificationInformationRepository.deleteAll(notifInfos);
            accountB2CRepository.deleteById(accountB2C.getId());
            smsNotificationService.sendSMSPP(accountB2C.getNumero(),String.format(applicationProperties.getSendSms().getAccountDeletion().getSmsContent(),msisdn), Constants.ORANGE_ET_MOI);
        });

    }

    private void deleteRattachementLigne(String msisdn) {
        Optional<RattachementLigne> numeroRattache = rattachementLigneRepository.findByNumero(msisdn);
        numeroRattache.ifPresent(rattachementLigne -> {
            rattachementLigneRepository.deleteById(rattachementLigne.getId());
        });
    }

    private void deleteLigneRattacheForPrincipal(AccountB2C principal){
        List<RattachementLigne> allByAccountB2C = rattachementLigneRepository.findAllByAccountB2C(principal);
        allByAccountB2C.forEach(rattachementLigne -> {
            rattachementLigneRepository.deleteById(rattachementLigne.getId());
        });
    }

}
