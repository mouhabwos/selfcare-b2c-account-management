package sn.sonatel.dsi.dif.selfcare.b2c.service.client.fallback;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.BoosterManager;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.dto.BoosterPromo;

public interface BoosterClientFallback {
    Logger log = LoggerFactory.getLogger(BoosterManager.class);

    default void applyBooster(String msisdn, String amount, String target, String ppi, Throwable cause) {
        log.info("Error when trying to apply welcome booster for {} with cause {}", msisdn, cause);
    }

    default ResponseEntity<List<BoosterPromo>> getActiveWelcomeBoosterValue(String msisdn, String code, String trigger, Throwable cause) {
        log.info("Error when trying to get Welcome Booster Status with cause {0}", cause);
        return ResponseEntity.unprocessableEntity().build();
    }
}
