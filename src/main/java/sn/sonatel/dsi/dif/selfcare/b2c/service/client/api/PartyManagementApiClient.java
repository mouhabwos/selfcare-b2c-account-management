package sn.sonatel.dsi.dif.selfcare.b2c.service.client.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.ApiManagementAuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OrganizationInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory.PartyManagementClientFallbackFactory;

@ApiManagementAuthorizedFeignClient(name = "${application.api-management.party-management.name}", url="${application.api-management.url-api.base-url}/"+"${application.api-management.party-management.name}",fallbackFactory = PartyManagementClientFallbackFactory.class)
public interface PartyManagementApiClient {

    @GetMapping(value = "${application.api-management.party-management.url-individual-information}")
    ResponseEntity<IndividualInformation> getIndividualInformation(@PathVariable("msisdn") String msisdn);

    @GetMapping(value = "${application.api-management.party-management.url-organization-information}")
    ResponseEntity<OrganizationInformation> getOrganizationInformation(@PathVariable("msisdn") String msisdn);
}
