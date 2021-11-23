package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedUserFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory.ServiceOTPFallBackFactory;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;

import javax.validation.Valid;
import java.util.Map;

@AuthorizedUserFeignClient(name = "${application.selfcare-otp}", fallbackFactory = ServiceOTPFallBackFactory.class)
public interface ServicesOTP {

    @PostMapping(Constants.URL_CHECK_CODE_OTP)
    ResponseEntity<CodeOTPCheckDTO> checkOTP(@Valid @RequestBody CodeOTPCheckDTO checkVM);

    @PostMapping( Constants.URL_SEND_MESSAGE)
    boolean generateMessage(@Valid @RequestBody MessageVM messageVM);

    @GetMapping(Constants.URL_CHECK_VALID_REQUEST_OTP)
    ResponseEntity<Map<String, Boolean>> registerCheckValidRequest(@PathVariable("msisdn") String msisdn);

}
