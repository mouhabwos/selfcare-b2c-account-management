package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserDTOExploitant;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceUAA;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import javax.validation.Valid;

@Service
public class SelfcareUAAService {

    private final ServiceUAA serviceUAA;

    public SelfcareUAAService(ServiceUAA serviceUAA) {

        this.serviceUAA = serviceUAA;
    }

    public  ResponseEntity regiserAccount(ManagedUserVM managedUserVM) {

        return serviceUAA.register(managedUserVM);
    }

    public void updateUser(@Valid @RequestBody UserDTOExploitant userDTO) {
       this.serviceUAA.updateUser(userDTO);
    }
}
