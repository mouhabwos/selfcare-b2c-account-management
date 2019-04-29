package sn.sonatel.dsi.dif.selfcare.b2c.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class OTPService {

    private final Logger log = LoggerFactory.getLogger(OTPService.class);




    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;



    /**
     * Generate a OTP code for User to reset password
     * @param login
     */
   public CodeOTPCheckDTO checkOPT(String msisdn, String code) {
        log.debug("checkOPT {}", msisdn);
       CodeOTPCheckDTO codeOTPCheckDTO = new CodeOTPCheckDTO();

       codeOTPCheckDTO.setMsisdn(msisdn);
       codeOTPCheckDTO.setCode(code);
        HttpEntity<CodeOTPCheckDTO> request = new HttpEntity<>(codeOTPCheckDTO);
       codeOTPCheckDTO = restTemplate.postForObject(applicationProperties.getUrlOtp()+"/api/code-otp-infos/check", request, CodeOTPCheckDTO.class);

        return  codeOTPCheckDTO;

    }

}
