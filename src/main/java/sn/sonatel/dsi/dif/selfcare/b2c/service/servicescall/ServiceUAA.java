package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserDTOExploitant;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory.ServiceUAAFallBackFactory;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ResetPasswordVM;

import javax.validation.Valid;

@AuthorizedFeignClient(name = "${application.selfcare-b2c-uaa}", fallbackFactory = ServiceUAAFallBackFactory.class)
public interface ServiceUAA {

    @PostMapping(Constants.REGISTER_ACCOUNT)
    ResponseEntity register(@Valid @RequestBody ManagedUserVM managedUserVM);

    @PutMapping(Constants.UAA_UPDATE_USERS)
    ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTOExploitant userDTO) ;

    @PostMapping(Constants.UAA_RESET_PASSWORD)
    ResponseEntity resetPasswordB2C(@RequestHeader("X-UUID") String uuid, @Valid @RequestBody ResetPasswordVM resetPasswordVM);

    }
