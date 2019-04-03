package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

@Service
public class SelfcareUAAService {


    @Qualifier("loadBalancedRestTemplate")
    private final RestTemplate restTemplate;

    public SelfcareUAAService(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> disableUser(String username){

        ResponseEntity<String> responseEntity = restTemplate
            .getForEntity(Constants.SELFCARE_UAA_SERVICE+""+Constants.GET_BLOQUER_ABONNE+username, String.class);

        if(responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {

            return responseEntity;

        }else {

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

    }
}
