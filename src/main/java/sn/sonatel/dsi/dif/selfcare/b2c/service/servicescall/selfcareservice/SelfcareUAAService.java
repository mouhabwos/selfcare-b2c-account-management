package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceUAA;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

@Service
public class SelfcareUAAService {

    private final ServiceUAA serviceUAA;

    public SelfcareUAAService(ServiceUAA serviceUAA) {

        this.serviceUAA = serviceUAA;
    }

    public  ResponseEntity regiserAccount(ManagedUserVM managedUserVM) {

        return serviceUAA.register(managedUserVM);
    }
}
