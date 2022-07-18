package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;

import javax.validation.Valid;
import java.util.Map;


public interface ServiceOTPFallBack   {




     default ResponseEntity<CodeOTPCheckDTO> checkOTP(@Valid CodeOTPCheckDTO checkVM,Throwable throwable) {
        response(throwable);

        return ResponseEntity.ok().build();
    }



     default ResponseEntity<Map<String, Boolean>> registerCheckValidRequest(String msisdn,Throwable throwable) {
        response(throwable);
        return ResponseEntity.ok().build();
    }

     default ResponseEntity response(Throwable throwable){
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
