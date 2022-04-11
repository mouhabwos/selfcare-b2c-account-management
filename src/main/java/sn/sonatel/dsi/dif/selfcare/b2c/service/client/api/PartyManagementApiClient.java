package sn.sonatel.dsi.dif.selfcare.b2c.service.client.api;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.ApiManagementAuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.fallback.PartyManagementClientFallback;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OrganizationInformation;

@ApiManagementAuthorizedFeignClient(name = "${application.api-management.party-management.name}", url="${application.api-management.url-api.base-url}/"+"${application.api-management.party-management.name}")
public interface PartyManagementApiClient extends PartyManagementClientFallback {

    @GetMapping(value = "${application.api-management.party-management.url-individual-information}")
    @CircuitBreaker(name="getIndividualInformation", fallbackMethod="getIndividualInformation")
    ResponseEntity<IndividualInformation> getIndividualInformation(@PathVariable("msisdn") String msisdn);

    @GetMapping(value = "${application.api-management.party-management.url-organization-information}")
    @CircuitBreaker(name="getOrganizationInformation", fallbackMethod="getOrganizationInformation")
    ResponseEntity<OrganizationInformation> getOrganizationInformation(@PathVariable("msisdn") String msisdn);
}
