package sn.sonatel.dsi.dif.selfcare.b2c.service.servicesCall;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import javax.validation.Valid;
import java.util.List;

@AuthorizedFeignClient(name="selfcare-b2c-soap")
public interface IServiceSOAP {

    @RequestMapping("/api/soap/souscription")
    SouscriptionDto getSouscription(SOAPRequest soapRequest);

    @RequestMapping("/api/soap/information-client")
    List<AbonneDTO> getAbonne(SOAPRequest soapRequest);
}
