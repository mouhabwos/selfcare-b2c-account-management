package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserDTOExploitant;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory.ServiceUAAFallBackFactory;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import javax.validation.Valid;

@AuthorizedFeignClient(name = Constants.SELFCARE_UAA_SERVICE, fallbackFactory = ServiceUAAFallBackFactory.class)
public interface ServiceUAA {

    @PostMapping(Constants.REGISTER_ACCOUNT)
    ResponseEntity register(@Valid @RequestBody ManagedUserVM managedUserVM);

    @PutMapping(Constants.UAA_UPDATE_USERS)
    ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTOExploitant userDTO) ;

    }
