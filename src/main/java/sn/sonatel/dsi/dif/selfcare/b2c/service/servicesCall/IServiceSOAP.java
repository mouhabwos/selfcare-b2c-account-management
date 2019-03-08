package sn.sonatel.dsi.dif.selfcare.b2c.service.servicesCall;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import javax.validation.Valid;

@FeignClient(name="selfcare-b2c-soap")
public interface IServiceSOAP {

    @RequestMapping("/api/soap/souscription")
    SouscriptionDto getSouscription(@Valid @RequestBody SOAPRequest soapRequest);

    @RequestMapping("/api/soap/souscription")
    AbonneDTO getAbonne(@Valid @RequestBody SOAPRequest soapRequest);
}
