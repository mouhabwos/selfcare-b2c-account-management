package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import org.springframework.http.HttpStatus;
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

        return throwableCall();
    }

    @Override
    public ResponseEntity<AbonneDTO> getAbonne(SOAPRequest msisdn) {

        return throwableCall();
    }

        private ResponseEntity throwableCall(){

            if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 500){

                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

            }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 503){

                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }

            return ResponseEntity.ok().build();
        }


}
