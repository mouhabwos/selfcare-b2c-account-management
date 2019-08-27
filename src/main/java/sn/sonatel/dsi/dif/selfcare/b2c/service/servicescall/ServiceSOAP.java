package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedUserFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory.ServiceSOAPFallBackFactory;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import javax.validation.Valid;

    @AuthorizedUserFeignClient( name = Constants.SELFCARE_B2C_SOAP_SERVICE, fallbackFactory = ServiceSOAPFallBackFactory.class)
    public interface ServiceSOAP {

    @PostMapping(Constants.GET_SOUSCRIPTION_ABONNE)
    ResponseEntity<SouscriptionDto> getSouscription(@Valid @RequestBody SOAPRequest msisdn);

    @PostMapping(Constants.GET_ABONNE)
    ResponseEntity<AbonneDTO> getAbonne(@Valid @RequestBody SOAPRequest msisdn);


    }
