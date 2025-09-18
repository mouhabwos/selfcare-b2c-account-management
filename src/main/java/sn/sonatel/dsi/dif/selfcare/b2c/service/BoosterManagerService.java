package sn.sonatel.dsi.dif.selfcare.b2c.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.CustomerOfferService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.BoosterClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.booster.dto.BoosterPromo;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;

@Service
public class BoosterManagerService {

    private final BoosterClient boosterClient;

    private final CustomerOfferService customerOfferService;

    public BoosterManagerService(BoosterClient boosterClient, CustomerOfferService customerOfferService) {
        this.boosterClient = boosterClient;
        this.customerOfferService = customerOfferService;
    }

    public List<BoosterPromo> getActiveWelcomeBoosterValue(String msisdn, String trigger) {
        CustomerOffer customerOffer = customerOfferService.getCustomerOffer(msisdn);

        ResponseEntity<List<BoosterPromo>> activeWelcomeBoosterValue = boosterClient.getActiveWelcomeBoosterValue(
            msisdn,
            customerOffer.getOfferCode(),
            trigger
        );
        List<BoosterPromo> activeWelcomeBoosterValueBody = activeWelcomeBoosterValue.getBody();
        if (activeWelcomeBoosterValueBody != null && !activeWelcomeBoosterValueBody.isEmpty()) {
            return activeWelcomeBoosterValueBody;
        }

        return new ArrayList<>();
    }
}
