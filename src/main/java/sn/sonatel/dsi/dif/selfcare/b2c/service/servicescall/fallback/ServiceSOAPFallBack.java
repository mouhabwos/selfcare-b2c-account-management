package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceSOAP;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;


public class ServiceSOAPFallBack implements ServiceSOAP {

    private final Throwable throwable;

    public ServiceSOAPFallBack(Throwable throwable) {

        this.throwable = throwable;
    }

    @Override
    public ResponseEntity<SouscriptionDto> getSouscription(SOAPRequest msisdn) {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AbonneDTO> getInformationClient(SOAPRequest msisdn) {

        return ResponseEntity.ok().build();
    }




}
