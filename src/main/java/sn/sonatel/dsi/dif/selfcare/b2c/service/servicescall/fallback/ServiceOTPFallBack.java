package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;

import javax.validation.Valid;


public class ServiceOTPFallBack implements ServicesOTP {

    private final Throwable throwable;

    public ServiceOTPFallBack(Throwable throwable) {

        this.throwable = throwable;
    }


    @Override
    public ResponseEntity<CodeOTPCheckDTO> checkOTP(@Valid CodeOTPCheckDTO checkVM) {
        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 500){

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        }else if(throwable instanceof FeignException && ((FeignException) throwable).status() == 503){

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public boolean generateMessage(@Valid MessageVM messageVM) {
        return false;
    }
}
