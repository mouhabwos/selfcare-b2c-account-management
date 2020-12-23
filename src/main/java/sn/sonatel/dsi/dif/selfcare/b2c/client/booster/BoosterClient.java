package sn.sonatel.dsi.dif.selfcare.b2c.client.booster;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.WelcomeBoosterStatus;

import java.util.List;

@AuthorizedFeignClient( name = Constants.SELFCARE_BOOSTER_SERVICE, fallbackFactory = BoosterClientFallbackFactory.class)
public interface BoosterClient {

    @PutMapping("/api/boosters/welcome-booster/{msisdn}")
    ResponseEntity<Void> applyWelcomeBooster(@PathVariable(name = "msisdn") String msisdn);

    @GetMapping("/api/boosters/welcome-booster-active")
    ResponseEntity<List<WelcomeBoosterStatus>> getActiveWelcomeBoosterValue();
}
