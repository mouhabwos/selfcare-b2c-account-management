package sn.sonatel.dsi.dif.selfcare.b2c.service.client.api;


import org.springframework.cloud.openfeign.FeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallbackfactory.CustomerOfferClientFallbackFactory;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
@FeignClient(name="${accountManagement_.name:accountManagement}", url="${accountManagement_.url:"+ Constants.API_MANAGEMENT +Constants.ENDPOINT_API_MANAGEMENT+"}", fallbackFactory = CustomerOfferClientFallbackFactory.class)
public interface CustomerOfferApiClient extends CustomerOfferApi {
}
