package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallbackfactory;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.CustomerOfferClientFallback;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
@Component
public class CustomerOfferClientFallbackFactory implements FallbackFactory<CustomerOfferApiClient> {

    @Override
    public CustomerOfferApiClient create(Throwable throwable) {
        return new CustomerOfferClientFallback(throwable);
    }

}
