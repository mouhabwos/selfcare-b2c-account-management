package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallbackfactory;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceGateway;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.ServiceGatewayFallBack;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */

@Component
public class ServiceGatewayFallBackFactory implements FallbackFactory<ServiceGateway> {

    /**
     *
     * @param throwable
     * @return response exception
     *
     * @author BOUYA KANDE
     * @since 1.1.4
     */
    @Override
    public ServiceGateway create(Throwable throwable) {
        return new ServiceGatewayFallBack(throwable);
    }


}
