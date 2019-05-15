package sn.sonatel.dsi.dif.selfcare.b2c.service;

/**
 * Service Interface for managing CodeOTPInfos.
 */
public interface CaptchaService {


    /**
     * Save a codeOTPInfos.
     *
     * @param token the captcha token
     * @return the persisted entity
     */
   boolean verifyCaptcha(String token);



}
