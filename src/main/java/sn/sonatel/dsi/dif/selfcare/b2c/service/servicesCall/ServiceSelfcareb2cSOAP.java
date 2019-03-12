package sn.sonatel.dsi.dif.selfcare.b2c.service.servicesCall;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.List;

public class ServiceSelfcareb2cSOAP {


    public static ResponseEntity<SouscriptionDto> getSouscription(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, HttpEntity<SOAPRequest> request){

        try {
            ResponseEntity<SouscriptionDto> response = restTemplate
                .exchange(Constants.SELFCARE_B2C_SOAP_SERVICE+""+Constants.GET_SOUSCRIPTION_ABONNE, HttpMethod.POST, request, SouscriptionDto.class);
            return response;

        }catch (Exception e){


        }
       return null;
    }

    public static ResponseEntity<List<AbonneDTO>> getAbonne(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate ,HttpEntity<SOAPRequest> request){

        try {
            ResponseEntity<List<AbonneDTO>> response = restTemplate
                .exchange(Constants.SELFCARE_B2C_SOAP_SERVICE+""+Constants.GET_ABONNE, HttpMethod.POST, request,(Class) List.class);
            return response;

        }catch (Exception e){


        }
        return null;
    }
}
