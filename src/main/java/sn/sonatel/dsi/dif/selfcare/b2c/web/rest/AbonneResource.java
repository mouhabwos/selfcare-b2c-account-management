package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AbonneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.CustomerOfferService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dac.dif.ds.juf.middleware.logging.Auditable;

@RestController
@RequestMapping("/api/abonne")
public class AbonneResource {

    private final Logger log = LoggerFactory.getLogger ( RattachementLigneResource.class );

    private final CustomerOfferService customerOfferService;
    private final AbonneService abonneService;

    public AbonneResource(CustomerOfferService customerOfferService, AbonneService abonneService) {

        this.customerOfferService = customerOfferService;
        this.abonneService = abonneService;
    }


    @Auditable(description = Message.Abonne.IS_POSPAID)
    @GetMapping("/is-postpaid/{msisdn}/{msisdn1}")
    @PreAuthorize("@customSecurityResolver.isAuthorized(#msisdn)")
    public ResponseEntity<Boolean> isPostpaid(@PathVariable String msisdn,@PathVariable String msisdn1) {
        log.debug ( "REST request to verify if  {} is pospaid ", msisdn1 );
            return new ResponseEntity<>(customerOfferService.isPostpaid(msisdn1), HttpStatus.OK);

    }


    /**
     * @author BOUYA KANDE
     * @since 1.1.4
     *
     */
    @Auditable(description = Message.Abonne.CUSTOMEROFFER)
    @GetMapping("/v1/customerOffer/{msisdn}")
    @Timed
    public ResponseEntity<CustomerOffer> getCustomerOffer(@PathVariable String msisdn){
        log.debug ( "REST request to get CustomerOffer : {}", msisdn );

        return ResponseEntity.ok(customerOfferService.getCustomerOffer(msisdn));

    }

    @Auditable(description = Message.Abonne.IS_ORANGE_NUMBER)
    @GetMapping("/v1/is-orange-number/{msisdn}")
    @Timed
    public ResponseEntity<Boolean> isOrangeNumber(@PathVariable String msisdn) {
        log.debug ( "REST request to get information  organization : {}", msisdn );
        return ResponseEntity.ok(abonneService.isOrangeNumber(msisdn));

    }

    @Auditable(description = Message.Abonne.BIRTHDATE)
    @GetMapping("/birthDate/{msisdn}")
    @PreAuthorize("@customSecurityResolver.isAuthorized(#msisdn)")
    @Timed
    public ResponseEntity<String> birthDate(@PathVariable String msisdn) {
        log.debug ( "REST request to get birthDate : {}", msisdn );
        return abonneService.getBirthDate(msisdn);

    }

}
