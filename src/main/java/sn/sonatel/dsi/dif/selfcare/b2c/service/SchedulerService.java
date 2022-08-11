package sn.sonatel.dsi.dif.selfcare.b2c.service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;

@Service
@Profile("chron")
public class SchedulerService {

    private final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private static final Long MAX_DELAI = 15L;

    private final SponseeRepository sponseeRepository;

    private final AccountB2CRepository accountB2CRepository;

    private final AbonneService abonneService;

    private final ApplicationProperties applicationProperties;

    public SchedulerService(
        SponseeRepository sponseeRepository,
        AccountB2CRepository accountB2CRepository,
        AbonneService abonneService,
        ApplicationProperties applicationProperties
    ) {
        this.sponseeRepository = sponseeRepository;
        this.accountB2CRepository = accountB2CRepository;
        this.abonneService = abonneService;
        this.applicationProperties = applicationProperties;
    }

    @Scheduled(cron = "${application.scheduler.cron-disabled-sponsee}")
    @Transactional
    public void disabledSponsee() {
        ZonedDateTime today = ZonedDateTime.now();

        List<Sponsee> allSponseeNoRegistered = sponseeRepository.findAllSponseeNoRegistered();

        for (Sponsee sponsee : allSponseeNoRegistered) {
            ZonedDateTime createdDate = sponsee.getCreatedDate();
            Duration duration = Duration.between(createdDate, today);
            long days = duration.toDays();

            if (days >= MAX_DELAI) {
                sponsee.setEnabled(false);
                sponseeRepository.save(sponsee);
            }
        }
    }

    @Scheduled(cron = "${application.scheduler.cron-update-firstname-lastname}")
    @Transactional
    public void updateFirstnameAndLastname() {
        log.debug(" Service for updating the FirstName and Lastname of users whose information is empty ");
        if (applicationProperties.getScheduler().isCronUpdateFirstnameLastnameActivated()) {
            int limit = Integer.parseInt(applicationProperties.getScheduler().getNumberOfRowsToReturn());

            List<AccountB2C> emptyFirstnameAndLastname = accountB2CRepository.findAllAccountB2CWithEmptyFirstnameOrLastname(limit);
            log.debug(" Size of the list of uses recovered : {} ", emptyFirstnameAndLastname.size());
            emptyFirstnameAndLastname.forEach(accountB2C -> {
                log.debug(" Modification of user informations : {} ", accountB2C);
                AbonneDTO abonne = abonneService.getInformationAbonne(accountB2C.getNumero());
                if (!abonne.getPrenomAbonne().equals("")) {
                    accountB2C.setFirstName(abonne.getPrenomAbonne());
                    accountB2C = accountB2CRepository.save(accountB2C);
                }
                if (!abonne.getNomAbonne().equals("")) {
                    accountB2C.setLastName(abonne.getNomAbonne());
                    accountB2CRepository.save(accountB2C);
                }
            });
        }
    }
}
