package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.OTPService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

/**
 * Created by centonni on 20/03/19.
 */
@Service
public class SelfcareSoapService {


    private final RestTemplate restTemplate;
    private final OTPService otpService;


    public SelfcareSoapService(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate, OTPService otpService) {
        this.restTemplate = restTemplate;
        this.otpService = otpService;
    }

    public ResponseEntity<SouscriptionDto> getSouscription(HttpEntity<SOAPRequest> request) {


        ResponseEntity<SouscriptionDto> responseEntity = restTemplate
            .exchange(Constants.SELFCARE_B2C_SOAP_SERVICE + "" + Constants.GET_SOUSCRIPTION_ABONNE, HttpMethod.POST, request, SouscriptionDto.class);

        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {

            return responseEntity;

        } else {

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

    }

    public ResponseEntity<AbonneDTO> getAbonne(String msisdn, String code) {

        CodeOTPCheckDTO codeOTPCheckDTO = otpService.checkOPT(msisdn, code);

        if(codeOTPCheckDTO != null){
            if (codeOTPCheckDTO.isValid()) {
                HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest(msisdn));

                ResponseEntity<AbonneDTO> responseEntity = restTemplate
                    .exchange(Constants.SELFCARE_B2C_SOAP_SERVICE + "" + Constants.GET_ABONNE, HttpMethod.POST, request, AbonneDTO.class);
                if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {


                    return responseEntity;

                } else {
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
                }
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }



    }


}
