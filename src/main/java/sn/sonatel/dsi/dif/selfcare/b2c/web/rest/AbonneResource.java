package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.CustomerOfferService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.utils.selfcarelogging.annotation.Auditable;

@RestController
@RequestMapping("/api/abonne")
public class AbonneResource {

    private final Logger log = LoggerFactory.getLogger ( RattachementLigneResource.class );

    private final SelfcareSoapService selfcareSoapService;
    private final CustomerOfferService customerOfferService;

    public AbonneResource(SelfcareSoapService selfcareSoapService, CustomerOfferService customerOfferService) {
        this.selfcareSoapService = selfcareSoapService;
        this.customerOfferService = customerOfferService;

    }


    @Auditable(description = Message.Abonne.SOUSC_USER)
    @GetMapping("/souscription/{msisdn}")
    //@PostAuthorize("@customSecurityResolver.isAuthorized(#msisdn)")
    public ResponseEntity<SouscriptionDto> getSouscription(@PathVariable String msisdn) {
        log.debug ( "REST request to get souscription : {}", msisdn );

        return  selfcareSoapService.getSouscription ( msisdn );

    }

    @Auditable(description = Message.Abonne.IS_POSPAID)
    @GetMapping("/is-postpaid/{msisdn}/{msisdn1}")
    @PostAuthorize("@customSecurityResolver.isAuthorized(#msisdn)")
    public ResponseEntity<Boolean> isPostpaid(@PathVariable String msisdn,@PathVariable String msisdn1) {
        log.debug ( "REST request to verify if  {} is pospaid ", msisdn1 );
            return new ResponseEntity<>(selfcareSoapService.isPostpaid(msisdn1), HttpStatus.OK);

    }



    @Auditable(description = Message.Abonne.INFO_ABONNE)
    @GetMapping("/information-abonne/{msisdn}/{code}")
    public ResponseEntity<AbonneDTO> getAbonne(@PathVariable String msisdn,@PathVariable String code) {
        log.debug ( "REST request to get abonne : {}", msisdn );

        return selfcareSoapService.getAbonne(msisdn, code);

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


}
