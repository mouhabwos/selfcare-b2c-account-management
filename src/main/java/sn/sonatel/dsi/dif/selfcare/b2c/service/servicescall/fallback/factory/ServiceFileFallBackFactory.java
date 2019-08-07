package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceFile;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.ServiceFileFallBack;

@Component
public class ServiceFileFallBackFactory implements FallbackFactory<ServiceFile> {

    @Override
    public ServiceFile create(Throwable throwable) {
        return new ServiceFileFallBack(throwable);
    }

}
