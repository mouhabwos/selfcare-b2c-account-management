package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
public class PartyManagementClientFallback implements PartyManagementApiClient {

    private final Logger log = LoggerFactory.getLogger(PartyManagementClientFallback.class);

    private final Throwable throwable;

    public PartyManagementClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ResponseEntity<IndividualInformation> getIndividualInformation(String msisdn) {
        log.debug("get individual information fallback for customer {} with following error : {} ",msisdn,throwable);
        return responseBuilder();
    }

    private ResponseEntity responseBuilder(){

        if(throwable.getClass() == HystrixTimeoutException.class){

            log.debug(" @@@@@@@@@ Error HystrixTimeoutException  PARTY Management SERVICE TIME OUT @@@@@@@@@@ ");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("");
        }
        if(throwable instanceof FeignException && ((FeignException) throwable).status() == 401){

            log.debug("Error status 404 API Management SERVICE UNAUTHORIZED : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
        } else
        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {

            log.debug("Error status 400 API Management BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 404){

            log.debug("Error status 404 SERVICE UNAVAILABLE API Management  : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 503){

            log.debug("Error status 503 API Management SERVICE UNAVAILABLE : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 403){

            log.debug("Error status 404 API Management SERVICE FORBIDDEN : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        } else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 504){

            log.debug("Error status 504 API Management SERVICE TIME OUT : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("");
        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 500){

            log.debug("Error status 500 API Management INTERNAL SERVER : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        }

        log.debug("default response status 500 : {} ", throwable.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-/-((-_-))-/-");
    }

}
