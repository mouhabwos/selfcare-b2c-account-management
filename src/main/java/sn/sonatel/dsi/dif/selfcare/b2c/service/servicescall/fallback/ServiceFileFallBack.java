package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceFile;


public class ServiceFileFallBack implements ServiceFile {

    private final Throwable throwable;

    public ServiceFileFallBack(Throwable throwable) {

        this.throwable = throwable;
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String filePath) {

        return ResponseEntity.ok().build();

    }


}
