package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice;

import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;

@Service
public class SelfcareOTPService {

    private final ServicesOTP servicesOTP;


    public SelfcareOTPService(ServicesOTP servicesOTP) {
        this.servicesOTP = servicesOTP;
    }


    public void generateMessage(MessageVM messageVM){
        servicesOTP.generateMessage( messageVM);
    }

    public CodeOTPCheckDTO checkOPT(String msisdn, String code) {

        CodeOTPCheckDTO codeOTPCheckDTO = new CodeOTPCheckDTO();

        codeOTPCheckDTO.setMsisdn(msisdn);
        codeOTPCheckDTO.setCode(code);

        codeOTPCheckDTO = servicesOTP.checkOTP(codeOTPCheckDTO).getBody();

        return  codeOTPCheckDTO;

    }

}
