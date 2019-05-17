package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;

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
    //@PostAuthorize("@customSecurityResolver.isAuthorized(#msisdn)")
    public ResponseEntity<SouscriptionDto> getSouscription(@PathVariable String msisdn) {
        log.debug ( "REST request to get souscription : {}", msisdn );

        return  selfcareSoapService.getSouscription ( msisdn );

    }


    @Auditable(description = Message.Abonne.INFO_ABONNE)
    @GetMapping("/information-abonne/{msisdn}/{code}")
    public ResponseEntity<AbonneDTO> getAbonne(@PathVariable String msisdn,@PathVariable String code) {
        log.debug ( "REST request to get abonne : {}", msisdn );

        return selfcareSoapService.getAbonne(msisdn, code);

    }


}
