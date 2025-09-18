package sn.sonatel.dsi.dif.selfcare.b2c.service.client.fallback;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
public interface CustomerOfferClientFallback {
    Logger log = LoggerFactory.getLogger(CustomerOfferClientFallback.class);

    default ResponseEntity<CustomerOffer> getCustomerOffer(String msisdn, Throwable throwable) {
        log.debug("getcustomerOffer fallback for customer {} with following error : {} ", msisdn, throwable);
        return responseBuilder(throwable);
    }

    default ResponseEntity<CustomerOffer> responseBuilder(Throwable throwable) {
        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {
            log.debug("Error status 400 API Management BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if (throwable instanceof FeignException && ((FeignException) throwable).status() == 500) {
            log.debug("Error status 500 API Management INTERNAL SERVER : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        } else if (throwable instanceof FeignException && ((FeignException) throwable).status() == 503) {
            log.debug("Error status 503 API Management SERVICE UNAVAILABLE : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        } else if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
            log.debug("Error status 404 SERVICE UNAVAILABLE API Management  : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else if (throwable instanceof FeignException && ((FeignException) throwable).status() == 401) {
            log.debug("Error status 404 API Management SERVICE UNAUTHORIZED : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else if (throwable instanceof FeignException && ((FeignException) throwable).status() == 403) {
            log.debug("Error status 404 API Management SERVICE FORBIDDEN : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } else if (throwable instanceof FeignException && ((FeignException) throwable).status() == 504) {
            log.debug("Error status 504 API Management SERVICE TIME OUT : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
