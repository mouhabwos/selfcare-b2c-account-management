package sn.sonatel.dsi.dif.selfcare.b2c.service.client.fallback;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OrganizationInformation;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
public interface PartyManagementClientFallback {
    Logger log = LoggerFactory.getLogger(PartyManagementClientFallback.class);

    default ResponseEntity<IndividualInformation> getIndividualInformation(String msisdn, Throwable throwable) {
        log.debug("get individual information fallback for customer {} with following error : {} ", msisdn, throwable);
        return responseBuilder(throwable);
    }

    default ResponseEntity<OrganizationInformation> getOrganizationInformation(String msisdn, Throwable throwable) {
        log.debug("get Organization information fallback for customer {} with following error : {} ", msisdn, throwable);
        return responseBuilder(throwable);
    }

    default <T> ResponseEntity<T> responseBuilder(Throwable throwable) {
        if (throwable.getClass() == HystrixTimeoutException.class) {
            log.debug(" @@@@@@@@@ Error HystrixTimeoutException  PARTY Management SERVICE TIME OUT @@@@@@@@@@ ");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
        } else if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {
            log.debug("@@@@@@@@@@@ Error status 400 API Management BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
            log.debug("@@@@@@@@@@@ Error status 404 SERVICE UNAVAILABLE API Management  : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        log.debug("@@@@@@@@@@@ default response status 503 : {} ", throwable.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    }
}
