package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallbackfactory.ServiceSOAPFallBackFactory;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import javax.validation.Valid;

    @AuthorizedFeignClient( name = Constants.SELFCARE_B2C_SOAP_SERVICE, fallbackFactory = ServiceSOAPFallBackFactory.class)
    public interface ServiceSOAP {

    @PostMapping(Constants.GET_SOUSCRIPTION_ABONNE)
    ResponseEntity<SouscriptionDto> getSouscription(@Valid @RequestBody SOAPRequest msisdn);

    @PostMapping(Constants.GET_ABONNE)
    ResponseEntity<AbonneDTO> getInformationClient(@Valid @RequestBody SOAPRequest msisdn);


    }
