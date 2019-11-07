package sn.sonatel.dsi.dif.selfcare.b2c.client.booster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.BoosterManager;

public class BoosterClientFallback implements BoosterClient{

    private final Logger log = LoggerFactory.getLogger(BoosterManager.class);

    private final Throwable cause;

    public BoosterClientFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public ResponseEntity<Void> applyWelcomeBooster(String msisdn) {
        log.info("Error when trying to apply welcome booster for {} with cause {}",msisdn,cause);
        return ResponseEntity.unprocessableEntity().build();
    }
}
