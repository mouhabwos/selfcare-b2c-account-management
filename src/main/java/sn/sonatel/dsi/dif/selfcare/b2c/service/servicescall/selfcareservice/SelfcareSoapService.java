package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceSOAP;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

/**
 * Created by centonni on 20/03/19.
 */
@Service
public class SelfcareSoapService {

    private final SelfcareOTPService otpService;
    private final ServiceSOAP serviceSOAP;



    public SelfcareSoapService( SelfcareOTPService otpService, ServiceSOAP serviceSOAP) {

        this.otpService = otpService;
        this.serviceSOAP = serviceSOAP;
    }


    public ResponseEntity<AbonneDTO> getAbonne(String msisdn, String code) {

        CodeOTPCheckDTO codeOTPCheckDTO = otpService.checkOPT(msisdn, code);

        if(codeOTPCheckDTO != null){
            if (codeOTPCheckDTO.isValid()) {

                SOAPRequest soapRequest = new SOAPRequest(msisdn);

                return serviceSOAP.getAbonne(soapRequest);

            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

    }

    public ResponseEntity<SouscriptionDto> getSouscription(String msisdn) {

        SOAPRequest soapRequest = new SOAPRequest(msisdn);

        return serviceSOAP.getSouscription( soapRequest);

    }

}
