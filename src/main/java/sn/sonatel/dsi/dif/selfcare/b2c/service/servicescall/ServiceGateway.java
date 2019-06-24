package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.sonatel.dsi.dif.selfcare.b2c.client.AuthorizedUserFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallbackfactory.ServiceGatewayFallBackFactory;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
@AuthorizedUserFeignClient(name = Constants.SELFCARE_GATEWAY, fallbackFactory = ServiceGatewayFallBackFactory.class)
public interface ServiceGateway {

    @GetMapping(Constants.URL_GET_NUMERO_CLIENT)
    ResponseEntity<String> getNumeroClient(@PathVariable("msisdn") String msisdn);
}
