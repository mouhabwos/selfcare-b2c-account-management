package sn.sonatel.dsi.dif.selfcare.b2c.service.client.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.ApiManagementAuthorizedFeignClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.TroubleTicket;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback.factory.TroubleTicketClientFallBackFactory;

import javax.validation.Valid;
import java.util.List;

@ApiManagementAuthorizedFeignClient(name="${application.api-management.url-api.base-name}", url="${application.api-management.url-api.base-url}"+"${application.api-management.url-api.url-account-management}", fallbackFactory = TroubleTicketClientFallBackFactory.class)
public interface TroubleTicketApiClient {

    @GetMapping("/api/troubleTicket/v1/troubleTicket/{id}")
    ResponseEntity<TroubleTicket> getTroubleTicketById(@PathVariable("id") String id, @Valid @RequestParam(name = "type") TroubleTicket.TicketTypeEnum type);

    @GetMapping("/api/troubleTicket/v1/troubleTicket")
    ResponseEntity<List<TroubleTicket>> getTroubleTicketByMsisdn(@Valid @RequestParam(name = "publicKey") String publicKey, @Valid @RequestParam(name = "type") TroubleTicket.TicketTypeEnum type);

}
