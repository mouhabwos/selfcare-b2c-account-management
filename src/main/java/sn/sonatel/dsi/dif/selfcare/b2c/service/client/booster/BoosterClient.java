package sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedUserFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.dto.BoosterPromo;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.fallback.BoosterClientFallback;

import java.util.List;

@AuthorizedUserFeignClient( name = "${application.selfcare-b2c-booster-management}")
public interface BoosterClient extends BoosterClientFallback {

    @PutMapping("/api/boosters/booster/{msisdn}")
    @CircuitBreaker(name="applyBooster", fallbackMethod="applyBooster")
    void applyBooster(@PathVariable("msisdn") String msisdn, @RequestParam("amount") String amount, @RequestParam("trigger") String trigger, @RequestParam("ppi") String ppi);

    @GetMapping("/api/boosters/active-boosters")
    @CircuitBreaker(name="getActiveWelcomeBoosterValue", fallbackMethod="getActiveWelcomeBoosterValue")
    ResponseEntity< List<BoosterPromo> > getActiveWelcomeBoosterValue(@RequestParam("msisdn") String msisdn, @RequestParam("code") String code, @RequestParam(value = "trigger",required = true) String trigger);
}
