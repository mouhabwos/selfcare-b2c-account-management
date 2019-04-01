package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.List;

/**
 * Created by centonni on 20/03/19.
 */
@Service
public class SelfcareSoapService {

    private final RestTemplate restTemplate;

    public SelfcareSoapService(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<SouscriptionDto> getSouscription( HttpEntity<SOAPRequest> request){

        ResponseEntity<SouscriptionDto> responseEntity = ServiceSelfcareb2cSOAP.getSouscription(restTemplate,request);

        if(responseEntity.getStatusCode() == HttpStatus.OK) {

            return responseEntity;

        }else {

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        }

    }

    public ResponseEntity<List<AbonneDTO>> getAbonne(HttpEntity<SOAPRequest> request){

        ResponseEntity<List<AbonneDTO>> responseEntity = ServiceSelfcareb2cSOAP.getAbonne(restTemplate,request);
        if(responseEntity.getStatusCode() == HttpStatus.OK){

            return responseEntity;

        }else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

    }

}
