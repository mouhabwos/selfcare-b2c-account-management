package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceUAA;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import javax.validation.Valid;


public class ServiceUAAFallBack implements ServiceUAA {

    private final Logger log = LoggerFactory.getLogger(ServiceUAAFallBack.class);

    private final Throwable throwable;

    public ServiceUAAFallBack(Throwable throwable) {

        this.throwable = throwable;
    }

    @Override
    public ResponseEntity register(@Valid ManagedUserVM managedUserVM) {

        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {

            log.debug("Error status 400 SelfcareUAA BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 500){

            log.debug("Error status 500 SelfcareUAA BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 503){

            log.debug("Error status 503 SelfcareUAA BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        log.debug("----- Error ServiceUAAfallback Register user to SelfcareUAA with following error : {} ",throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-/-((-_-))-/-");
    }

}
