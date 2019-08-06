package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceFile;


public class ServiceFileFallBack implements ServiceFile {

    private final Logger log = LoggerFactory.getLogger(ServiceFileFallBack.class);

    private final Throwable throwable;

    public ServiceFileFallBack(Throwable throwable) {

        this.throwable = throwable;
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String filePath) {
        return responseBuilderFileManager();

    }

    @Override
    public ResponseEntity<String> fileUpload(MultipartFile file) {
        return responseBuilderFileManager();
    }

    private ResponseEntity responseBuilderFileManager(){

        if(throwable instanceof FeignException && ((FeignException) throwable).status() == 503){

            log.debug("Error status 503 Selfcare File Manager  SERVICE UNAVAILABLE : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        }else if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {

            log.debug("Error status 400 Selfcare File Manager BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");

        } else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 500){

            log.debug("Error status 500 Selfcare File Manager  INTERNAL SERVER : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        } else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 404){

            log.debug("Error status 404 SSelfcare File Manager  SERVICE NOT FOUND : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }



}
