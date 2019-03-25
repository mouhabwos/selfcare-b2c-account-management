package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

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

class ServiceSelfcareb2cSOAP {

    private ServiceSelfcareb2cSOAP() {
        //Default constructor
    }

    public static ResponseEntity<SouscriptionDto> getSouscription(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, HttpEntity<SOAPRequest> request){


            return restTemplate
                .exchange(Constants.SELFCARE_B2C_SOAP_SERVICE+""+Constants.GET_SOUSCRIPTION_ABONNE, HttpMethod.POST, request, SouscriptionDto.class);
    }

        public static ResponseEntity<List<AbonneDTO>> getAbonne(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate ,HttpEntity<SOAPRequest> request){

                return restTemplate
                    .exchange(Constants.SELFCARE_B2C_SOAP_SERVICE+""+Constants.GET_ABONNE, HttpMethod.POST, request,(Class) List.class);

        }

    public static ResponseEntity<String> getFormuleByMsisdn(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate ,String msisdn){

        String url = Constants.SELFCARE_B2C_SOAP_SERVICE+""+Constants.GET_FORMULE_BY_MSISDN+msisdn;

        return restTemplate.getForEntity(url, String.class);
    }



}
