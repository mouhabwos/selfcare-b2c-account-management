package sn.sonatel.dsi.dif.selfcare.b2c.service.servicesCall;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import javax.validation.Valid;


@FeignClient(name="selfcare-uaa")
public interface IServiceUAA {

    // consumes = "application/json"
    @RequestMapping("/api/register")
    void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM);
}
