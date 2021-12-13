package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceUAA;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.ServiceUAAFallBack;

@Component
public class ServiceUAAFallBackFactory implements FallbackFactory<ServiceUAA> {

    @Override
    public ServiceUAA create(Throwable throwable) {
        return new ServiceUAAFallBack(throwable);
    }


}
