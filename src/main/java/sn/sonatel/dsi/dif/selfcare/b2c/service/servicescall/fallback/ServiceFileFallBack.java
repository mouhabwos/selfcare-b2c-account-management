package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceFile;


public class ServiceFileFallBack implements ServiceFile {

    private final Throwable throwable;

    public ServiceFileFallBack(Throwable throwable) {

        this.throwable = throwable;
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String filePath) {

        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 500){

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 503){

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return ResponseEntity.ok().build();

    }


}
