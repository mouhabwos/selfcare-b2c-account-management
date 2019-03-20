package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.aop.logging.annotation.Auditable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceSelfcareb2cSOAP;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Message;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.List;

@RestController
@RequestMapping("/api/abonne")
public class AbonneResource {

    private final Logger log = LoggerFactory.getLogger ( RattachementLigneResource.class );


    @Qualifier("loadBalancedRestTemplate")
    private final RestTemplate restTemplate;

    public AbonneResource(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }


    @Auditable(description = Message.Abonne.SOUSC_USER)
    @GetMapping("/souscription/{msisdn}")
    public ResponseEntity<SouscriptionDto> getSouscription(@PathVariable String msisdn) {
        log.debug ( "REST request to get souscription : {}", msisdn );

        HttpEntity<SOAPRequest> request = new HttpEntity<> ( new SOAPRequest ( msisdn ) );
        try {

            ResponseEntity<SouscriptionDto> response = ServiceSelfcareb2cSOAP.getSouscription ( restTemplate, request );
            if (response.getStatusCode () == HttpStatus.NOT_FOUND) {
                return null;
            } else if (response.getStatusCode () == HttpStatus.OK) {
                String code = getCodeFormule ( msisdn );
                if (code != null) {
                    response.getBody ().setCodeOffre ( code );
                }
                return response;
            }

        } catch (Exception e) {

            log.debug ( "Exception get souscription abonne : {}", msisdn );
        }

        return null;

    }


    @Auditable(description = Message.Abonne.INFO_ABONNE)
    @GetMapping("/information-abonne/{msisdn}")
    public ResponseEntity<List<AbonneDTO>> getAbonne(@PathVariable String msisdn) {
        log.debug ( "REST request to get abonne : {}", msisdn );

        HttpEntity<SOAPRequest> request = new HttpEntity<> ( new SOAPRequest ( msisdn ) );

        try {

            ResponseEntity<List<AbonneDTO>> response = ServiceSelfcareb2cSOAP.getAbonne ( restTemplate, request );
            if (response.getStatusCode () == HttpStatus.NOT_FOUND) {

                return null;
            } else if (response.getStatusCode () == HttpStatus.OK) {

                return response;
            }

        } catch (Exception e) {

            log.debug ( "Exception get information abonne : {}", msisdn );
        }

        return null;


    }

    public String getCodeFormule(String msisdn) {
        log.debug ( "REST request to get abonne : {}", msisdn );


        try {

            ResponseEntity<String> response = ServiceSelfcareb2cSOAP.getFormuleByMsisdn ( restTemplate, msisdn );
            if (response.getStatusCode () == HttpStatus.NOT_FOUND) {

                return null;
            } else if (response.getStatusCode () == HttpStatus.OK) {

                return response.getBody ();
            }

        } catch (Exception e) {

            log.debug ( "Exception get information abonne : {}", msisdn );
        }

        return null;


    }


}
