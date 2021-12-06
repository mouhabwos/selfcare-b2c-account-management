package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.PartyManagementClientFallback;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
@Component
public class PartyManagementClientFallbackFactory implements FallbackFactory<PartyManagementApiClient> {

    @Override
    public PartyManagementApiClient create(Throwable throwable) {
        return new PartyManagementClientFallback(throwable);
    }

}
