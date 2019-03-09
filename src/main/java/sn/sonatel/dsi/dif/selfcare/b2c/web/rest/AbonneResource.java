package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicesCall.IServiceSOAP;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/abonne")
public class AbonneResource {

    private final Logger log = LoggerFactory.getLogger(RattachementLigneResource.class);

    private static final String ENTITY_NAME = "selfcareB2CAccountManagementAbonne";

    private final IServiceSOAP iServiceSOAP;

    public AbonneResource(IServiceSOAP iServiceSOAP) {
        this.iServiceSOAP = iServiceSOAP;
    }

    /**
     * GET  /souscription/:msisdn : get the "msisdn" souscriptiondto.
     *
     * @param msisdn the msisdn of the souscription to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the souscriptiondto, or with status 404 (Not Found)
     */
    @GetMapping("/souscription/{msisdn}")
    public ResponseEntity<SouscriptionDto> getSouscription(@PathVariable String msisdn) {
        log.debug("REST request to get souscription : {}", msisdn);

        SOAPRequest soapRequest = new SOAPRequest(msisdn);
        SouscriptionDto dto = iServiceSOAP.getSouscription(soapRequest);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * GET  /information-abonne/:msisdn : get the "msisdn" souscriptiondto.
     *
     * @param msisdn the msisdn of the Abonne to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the abonnedto, or with status 404 (Not Found)
     */
    @GetMapping("/information-abonne/{msisdn}")
    public ResponseEntity<List<AbonneDTO>> getAbonne(@PathVariable String msisdn) {
        log.debug("REST request to get abonne : {}", msisdn);

        SOAPRequest soapRequest = new SOAPRequest(msisdn);
        List<AbonneDTO> dto = iServiceSOAP.getAbonne(soapRequest);
        return ResponseEntity.ok().body(dto);
    }


}
