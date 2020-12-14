package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ValidationHmacService {

    private final Logger log = LoggerFactory.getLogger(ValidationHmacService.class);

    private static final String INDICATIF = "221";

    private final ApplicationProperties applicationProperties;

    public ValidationHmacService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * evol: 04/12/2019
     * validates HMAC from @link RegistrationRequest params
     * @param msisdn
     * @return
     * @author: Bouya
     */
    public boolean validateHmac(String hmac, String msisdn, String uuid) {

        log.trace("Validating HMAC RegistrationRequest: {} ", msisdn);
        return this.compareHmac(hmac, msisdn, uuid);

    }

        /**
     * compare and return the result between given hmac and computed one
     * @param hmac: the hmac to validate
     * @param msisdn : the msisdn for who the hmac was generated
     * @param uuid: the uuid of device
     * @return boolean
     * @author: bouya
     */
    public boolean compareHmac(String hmac, String msisdn, String uuid) {
        String computedHmac = SHA256Handler.encryptSHA256(String.format("%s%s%s%s", INDICATIF+msisdn, uuid, new SimpleDateFormat("dd/MM/yyyy").format(new Date()), applicationProperties.getHmacSecret()));
        return computedHmac.equals(hmac);
    }
}
