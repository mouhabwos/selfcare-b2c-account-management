package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.dif.selfcare.b2c.security.SecurityUtils;
import sn.sonatel.dsi.dif.selfcare.b2c.service.CaptchaService;

import java.util.HashMap;
import java.util.Map;

/**
 * Service Interface for managing CodeOTPInfos.
 */
@Service
@Transactional
public class CaptchaServiceImpl implements CaptchaService {

    private final Logger log = LoggerFactory.getLogger(CaptchaServiceImpl.class);


    private final RestTemplateBuilder restTemplateBuilder;

    private static final String GOOGLE_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public CaptchaServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    /**
     * Save a codeOTPInfos.
     *
     * @param token the captcha token
     * @return the persisted entity
     */
   public boolean verifyCaptcha(String token){

        log.info("checking captcha for user {}", SecurityUtils.getCurrentUserLogin());
        Map<String, String> body = new HashMap<>();
        body.put("secret", "6Lee1KAUAAAAACeeFVVFWvthryWPzwosA5hzxMDx");
        body.put("response", token);

        log.debug("Request body for recaptcha: {}", body);
        ResponseEntity<Map> recaptchaResponseEntity = restTemplateBuilder.build().postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL + "?secret={secret}&response={response}", body,
                Map.class, body);

        log.debug("Response from recaptcha: {}", recaptchaResponseEntity);
        Map<String, Object> responseBody = recaptchaResponseEntity.getBody();
        log.debug("Response from google: {}", responseBody);
        boolean recaptchaSucess = (Boolean) responseBody.get("success");

       boolean result=false;

       if (recaptchaSucess) {
            Float score= Float.parseFloat(responseBody.get("score").toString());
            if(score!=null && score>= 0.5 ) // C'est un humain probable
                result= true;

        }

       return result;

   }



}
