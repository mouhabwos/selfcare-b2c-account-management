package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.List;

@RestController
@RequestMapping("/api/abonne")
public class AbonneResource {

    private final Logger log = LoggerFactory.getLogger ( RattachementLigneResource.class );

    private final SelfcareSoapService selfcareSoapService;

    public AbonneResource(SelfcareSoapService selfcareSoapService) {
        this.selfcareSoapService = selfcareSoapService;
    }


    @Auditable(description = Message.Abonne.SOUSC_USER)
    @GetMapping("/souscription/{msisdn}")
    public ResponseEntity<SouscriptionDto> getSouscription(@PathVariable String msisdn) {
        log.debug ( "REST request to get souscription : {}", msisdn );

        HttpEntity<SOAPRequest> request = new HttpEntity<> ( new SOAPRequest ( msisdn ) );

        return selfcareSoapService.getSouscription ( request );

    }


    @Auditable(description = Message.Abonne.INFO_ABONNE)
    @GetMapping("/information-abonne/{msisdn}")
    public ResponseEntity<List<AbonneDTO>> getAbonne(@PathVariable String msisdn) {
        log.debug ( "REST request to get abonne : {}", msisdn );

        HttpEntity<SOAPRequest> request = new HttpEntity<> ( new SOAPRequest ( msisdn ) );

        return selfcareSoapService.getAbonne ( request );

    }


}
