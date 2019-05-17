package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallbackfactory.ServiceUAAFallBackFactory;

import javax.validation.Valid;

@AuthorizedFeignClient(name = Constants.SELFCARE_SERVICE_OTP, fallbackFactory = ServiceUAAFallBackFactory.class)
public interface ServicesOTP {

    @PostMapping(name = Constants.URL_CHECK_CODE_OTP)
    ResponseEntity<CodeOTPCheckDTO> registerOTP(@Valid @RequestBody CodeOTPCheckDTO checkVM);

}
