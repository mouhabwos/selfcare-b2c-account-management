package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallbackfactory;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceSOAP;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.ServiceSOAPFallBack;

@Component
public class ServiceSOAPFallBackFactory implements FallbackFactory<ServiceSOAP> {


    @Override
    public ServiceSOAP create(Throwable throwable) {
        return new ServiceSOAPFallBack(throwable);
    }


}
