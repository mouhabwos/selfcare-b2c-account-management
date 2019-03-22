package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import feign.FeignException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public class ServiceFileFallBack implements ServiceFile {

    private final Throwable cause;

    public ServiceFileFallBack( Throwable cause) {
        this.cause = cause;
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String filePath) {

        if (cause instanceof FeignException && ((FeignException) cause).status() == 500) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok().build();

    }


}
