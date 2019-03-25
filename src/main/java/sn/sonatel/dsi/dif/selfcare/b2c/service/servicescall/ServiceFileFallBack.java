package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ServiceFileFallBack implements ServiceFile {

    @Override
    public ResponseEntity<Resource> downloadFile(String filePath) {

        return ResponseEntity.ok().build();

    }


}
