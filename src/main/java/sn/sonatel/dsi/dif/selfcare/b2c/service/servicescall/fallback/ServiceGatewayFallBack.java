package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceGateway;


/**
 *
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
public class ServiceGatewayFallBack implements ServiceGateway {

    private final Logger log = LoggerFactory.getLogger(ServiceGatewayFallBack.class);

    private final Throwable throwable;

    public ServiceGatewayFallBack(Throwable throwable) {

        this.throwable = throwable;
    }

    @Override
    public ResponseEntity<String> getNumeroClient(String msisdn) {
        responseBuilder();
        return ResponseEntity.ok().build();
    }


    private ResponseEntity responseBuilder(){

        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {

            log.debug("Error status 400 Selfcare gateway BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 500){

            log.debug("Error status 500 Selfcare gateway INTERNAL SERVER : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 503){

            log.debug("Error status 503 Selfcare gateway SERVICE UNAVAILABLE : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 404){

            log.debug("Error status 404 Selfcare gateway SERVICE UNAVAILABLE : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
