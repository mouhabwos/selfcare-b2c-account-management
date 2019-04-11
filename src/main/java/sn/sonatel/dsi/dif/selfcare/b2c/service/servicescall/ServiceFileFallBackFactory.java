package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceFileFallBackFactory implements FallbackFactory<ServiceFile> {

    @Override
    public ServiceFile create(Throwable throwable) {
        return new ServiceFileFallBack(throwable);
    }

}
