package sn.sonatel.dsi.dif.selfcare.b2c.client.booster;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BoosterClientFallbackFactory implements FallbackFactory<BoosterClient> {
    @Override
    public BoosterClient create(Throwable throwable) {
        return new BoosterClientFallback(throwable);
    }
}
