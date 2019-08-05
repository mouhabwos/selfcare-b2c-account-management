package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedUserFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallbackfactory.ServiceFileFallBackFactory;

@AuthorizedUserFeignClient( name = Constants.SELFCARE_FILE_MANAGER_SERVICE, fallbackFactory = ServiceFileFallBackFactory.class)
public interface ServiceFile {
    @GetMapping(path = Constants.FILE_DOWNLOAD+"{filePath}")
    ResponseEntity<Resource> downloadFile(@PathVariable("filePath") String filePath);

    @PostMapping(Constants.FILE_UPLOAD)
    ResponseEntity<String> fileUpload(@RequestParam("file") MultipartFile file);
}
