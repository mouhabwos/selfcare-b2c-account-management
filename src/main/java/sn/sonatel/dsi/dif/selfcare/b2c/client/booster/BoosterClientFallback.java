package sn.sonatel.dsi.dif.selfcare.b2c.client.booster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.client.booster.dto.BoosterPromo;
import sn.sonatel.dsi.dif.selfcare.b2c.service.BoosterManager;

import java.util.List;

public class BoosterClientFallback implements BoosterClient{

    private final Logger log = LoggerFactory.getLogger(BoosterManager.class);

    private final Throwable cause;

    public BoosterClientFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public void applyBooster(String msisdn, String amount, String target,String ppi) {
        log.info("Error when trying to apply welcome booster for {} with cause {}",msisdn,cause);
     }

    @Override
    public ResponseEntity<List<BoosterPromo>> getActiveWelcomeBoosterValue(String msisdn, String code, String trigger) {
        log.info("Error when trying to get Welcome Booster Status with cause {}",cause);
        return ResponseEntity.unprocessableEntity().build();
    }
}
