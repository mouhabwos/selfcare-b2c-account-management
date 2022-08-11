package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;

@Service
public class SelfcareOTPService {

    private final ServicesOTP servicesOTP;

    public SelfcareOTPService(ServicesOTP servicesOTP) {
        this.servicesOTP = servicesOTP;
    }

    public CodeOTPCheckDTO checkOPT(String msisdn, String code) {
        CodeOTPCheckDTO codeOTPCheckDTO = new CodeOTPCheckDTO();

        codeOTPCheckDTO.setMsisdn(msisdn);
        codeOTPCheckDTO.setCode(code);

        codeOTPCheckDTO = servicesOTP.checkOTP(codeOTPCheckDTO).getBody();

        return codeOTPCheckDTO;
    }

    /**
     * Generate a OTP code for User to reset password
     * @param msisdn
     */
    public boolean checkRegisterValidity(String msisdn) {
        ResponseEntity<Map<String, Boolean>> mapResponseEntity = servicesOTP.registerCheckValidRequest(msisdn);

        Map<String, Boolean> response = mapResponseEntity.getBody();

        return response != null && response.get(Constants.VALIDE);
    }
}
