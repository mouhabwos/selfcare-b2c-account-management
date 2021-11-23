package sn.sonatel.dsi.dif.selfcare.b2c.client.booster;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedUserFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.client.booster.dto.BoosterPromo;

import java.util.List;

@AuthorizedUserFeignClient( name = "${application.selfcare-b2c-booster-management}", fallbackFactory = BoosterClientFallbackFactory.class)
public interface BoosterClient {

    @PutMapping("/api/boosters/booster/{msisdn}")
    void applyBooster(@PathVariable("msisdn") String msisdn, @RequestParam("amount") String amount, @RequestParam("trigger") String trigger, @RequestParam("ppi") String ppi);

    @GetMapping("/api/boosters/active-boosters")
    ResponseEntity< List<BoosterPromo> > getActiveWelcomeBoosterValue(@RequestParam("msisdn") String msisdn, @RequestParam("code") String code, @RequestParam(value = "trigger",required = true) String trigger);
}
