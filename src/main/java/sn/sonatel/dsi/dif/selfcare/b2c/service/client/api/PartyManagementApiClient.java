package sn.sonatel.dsi.dif.selfcare.b2c.service.client.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.ApiManagementAuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory.PartyManagementClientFallbackFactory;

@ApiManagementAuthorizedFeignClient(name = Constants.PARTY_MANAGEMENT, url="${application.api-management.url-api.base-url}/"+Constants.PARTY_MANAGEMENT,fallbackFactory = PartyManagementClientFallbackFactory.class)
public interface PartyManagementApiClient {

    @GetMapping(value = "/api/partyManagement/v1/individual/{msisdn}")
    ResponseEntity<IndividualInformation> getIndividualInformation(@PathVariable("msisdn") String msisdn);

}
