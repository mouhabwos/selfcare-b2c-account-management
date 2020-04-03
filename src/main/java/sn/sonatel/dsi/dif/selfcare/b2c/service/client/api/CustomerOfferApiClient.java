package sn.sonatel.dsi.dif.selfcare.b2c.service.client.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.ApiManagementAuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory.CustomerOfferClientFallbackFactory;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
@ApiManagementAuthorizedFeignClient(name="${application.api-management.url-api.base-name}", url="${application.api-management.url-api.base-url}"+"${application.api-management.url-api.url-account-management}", fallbackFactory = CustomerOfferClientFallbackFactory.class)
public interface CustomerOfferApiClient{

    @GetMapping(value = "/api/accountManagement/v1/customerOffer/{msisdn}")
    ResponseEntity<CustomerOffer> getCustomerOffer(@PathVariable("msisdn") String msisdn);
}
