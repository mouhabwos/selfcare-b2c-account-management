package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.client.booster.BoosterClient;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.BoosterTrigger;

@Service
public class BoosterManager {

    private final Logger log = LoggerFactory.getLogger(BoosterManager.class);

    private final BoosterClient boosterClient;

    public BoosterManager(BoosterClient boosterClient) {
        this.boosterClient = boosterClient;
    }

    @Async
    public void applyWelcomeBooster(String msisdn) {
        log.info("Request to apply welcome booster for client {} ", msisdn);
        this.boosterClient.applyBooster(msisdn,null, BoosterTrigger.FORM_INSCRIPTION.name(),null);
    }

}
