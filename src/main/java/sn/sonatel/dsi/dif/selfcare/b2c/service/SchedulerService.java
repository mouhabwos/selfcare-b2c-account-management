package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Profile("chron")
public class SchedulerService {

    private static final Long MAX_DELAI = 15L;

    private final SponseeRepository sponseeRepository;

    public SchedulerService(SponseeRepository sponseeRepository) {
        this.sponseeRepository = sponseeRepository;
    }


    @Scheduled(cron = "${application.scheduler.cron-disabled-sponsee}")
    @Transactional
    public void disabledSponsee(){
        ZonedDateTime today = ZonedDateTime.now();

        List<Sponsee> allSponseeNoRegistered = sponseeRepository.findAllSponseeNoRegistered();

        for (Sponsee sponsee : allSponseeNoRegistered) {

            ZonedDateTime createdDate = sponsee.getCreatedDate();
            Duration duration = Duration.between(createdDate, today);
            long days = duration.toDays();

            if(days >= MAX_DELAI){
                sponsee.setEnabled(false);
                sponseeRepository.save(sponsee);
            }
        }
    }
}
