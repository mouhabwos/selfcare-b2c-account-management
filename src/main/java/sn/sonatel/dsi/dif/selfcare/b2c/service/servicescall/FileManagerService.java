package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

@Service
public class FileManagerService {

    @Qualifier("loadBalancedRestTemplate")
    private final RestTemplate restTemplate;

    public FileManagerService(@Qualifier("loadBalancedRestTemplate")RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<?> downloadFile(String filePath){

        String url = Constants.SELFCARE_FILE_MANAGER_SERVICE+""+Constants.FILE_DOWNLOAD+filePath;

        return restTemplate.getForEntity(url, Class.class);

    }

}
