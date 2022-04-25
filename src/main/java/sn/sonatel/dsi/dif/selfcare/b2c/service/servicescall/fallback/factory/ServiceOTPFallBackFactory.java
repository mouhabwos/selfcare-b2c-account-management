package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.ServiceOTPFallBack;

@Component
public class ServiceOTPFallBackFactory implements FallbackFactory<ServicesOTP> {

    @Override
    public ServicesOTP create(Throwable throwable) {
        return new ServiceOTPFallBack(throwable);
    }

}
