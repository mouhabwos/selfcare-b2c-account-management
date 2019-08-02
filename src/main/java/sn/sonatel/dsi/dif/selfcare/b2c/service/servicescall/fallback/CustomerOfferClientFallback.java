package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
public class CustomerOfferClientFallback implements CustomerOfferApiClient {

    private final Logger log = LoggerFactory.getLogger(CustomerOfferClientFallback.class);

    private final Throwable throwable;

    public CustomerOfferClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    private ResponseEntity responseBuilder(){

        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {

            log.debug("Error status 400 API Management BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 500){

            log.debug("Error status 500 API Management INTERNAL SERVER : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 503){

            log.debug("Error status 503 API Management SERVICE UNAVAILABLE : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 404){

            log.debug("Error status 404 SERVICE UNAVAILABLE API Management  : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");

        } else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 401){

            log.debug("Error status 404 API Management SERVICE UNAVAILABLE : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        }

        if(throwable instanceof FeignException && ((FeignException) throwable).status() == 403){

            log.debug("Error status 404 API Management SERVICE UNAVAILABLE : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @Override
    public ResponseEntity<CustomerOffer> customerOffer(String msisdn) {
        return responseBuilder();
    }
}
