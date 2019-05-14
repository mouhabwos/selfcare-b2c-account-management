package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import javax.validation.Valid;

@AuthorizedFeignClient(name = Constants.SELFCARE_UAA_SERVICE)
public interface ServiceUAA {

    @PostMapping(name = Constants.REGISTER_ACCOUNT)
    ResponseEntity register(@Valid @RequestBody ManagedUserVM managedUserVM);

}
