package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.List;

@RestController
@RequestMapping("/api/abonne")
public class AbonneResource {

    private final Logger log = LoggerFactory.getLogger(RattachementLigneResource.class);

    private final SelfcareSoapService selfcareSoapService;

    public AbonneResource(SelfcareSoapService selfcareSoapService) {
        this.selfcareSoapService = selfcareSoapService;
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

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest(msisdn));
        try {

            ResponseEntity<SouscriptionDto> response = selfcareSoapService.getSouscription(request);
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            else if (response.getStatusCode() == HttpStatus.OK) {

                return response;
            }

        }catch (Exception e){

            log.debug("Exception get souscription abonne : {}", msisdn);
        }

        return null;

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

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest(msisdn));

        try {

            ResponseEntity<List<AbonneDTO>> response = selfcareSoapService.getAbonne( request);

            if (response.getStatusCode() == HttpStatus.OK) {

                return response;
            }

        }catch (Exception e){

            log.debug("Exception get information abonne : {}", msisdn);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();


    }


}
