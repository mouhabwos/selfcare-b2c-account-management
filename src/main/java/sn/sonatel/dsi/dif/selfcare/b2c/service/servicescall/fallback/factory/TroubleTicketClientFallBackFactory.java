package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.TroubleTicketApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.TroubleTicketClientFallBack;

@Component
public class TroubleTicketClientFallBackFactory implements FallbackFactory<TroubleTicketApiClient> {
    @Override
    public TroubleTicketApiClient create(Throwable throwable) {
        return new TroubleTicketClientFallBack(throwable);
    }
}
