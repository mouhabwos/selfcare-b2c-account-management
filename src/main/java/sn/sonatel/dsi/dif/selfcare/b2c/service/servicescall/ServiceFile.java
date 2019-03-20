package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import feign.Param;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

@AuthorizedFeignClient(name = Constants.SELFCARE_FILE_MANAGER_SERVICE)
public interface ServiceFile {
    @RequestMapping(method = RequestMethod.GET, path = Constants.FILE_DOWNLOAD+"{filePath}")
    ResponseEntity<Resource> downloadFile(@PathVariable("filePath") String filePath);
}
