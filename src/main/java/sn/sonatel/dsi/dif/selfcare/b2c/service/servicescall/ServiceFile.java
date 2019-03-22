package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;


import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

@AuthorizedFeignClient( name = Constants.SELFCARE_FILE_MANAGER_SERVICE, fallbackFactory = ServiceFileFallBackFactory.class)
public interface ServiceFile {
    @GetMapping(path = Constants.FILE_DOWNLOAD+"{filePath}")
    ResponseEntity<Resource> downloadFile(@PathVariable("filePath") String filePath);
}
