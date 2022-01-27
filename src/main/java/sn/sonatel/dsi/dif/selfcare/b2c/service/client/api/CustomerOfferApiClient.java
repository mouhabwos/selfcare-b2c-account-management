package sn.sonatel.dsi.dif.selfcare.b2c.service.client.api;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.ApiManagementAuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.fallback.CustomerOfferClientFallback;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
@ApiManagementAuthorizedFeignClient(name="${application.api-management.url-api.base-name}", url="${application.api-management.url-api.base-url}"+"${application.api-management.url-api.url-account-management}")
public interface CustomerOfferApiClient extends CustomerOfferClientFallback {

    @GetMapping(value = "/api/accountManagement/v1/customerOffer/{msisdn}")
    @CircuitBreaker(name="getCustomerOffer", fallbackMethod="getCustomerOffer")
    ResponseEntity<CustomerOffer> getCustomerOffer(@PathVariable("msisdn") String msisdn);
}
