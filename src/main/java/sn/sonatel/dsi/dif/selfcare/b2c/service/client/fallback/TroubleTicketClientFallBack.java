package sn.sonatel.dsi.dif.selfcare.b2c.service.client.fallback;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.TroubleTicket;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

public interface TroubleTicketClientFallBack{

      Logger log = LoggerFactory.getLogger(TroubleTicketClientFallBack.class);

    default ResponseEntity<TroubleTicket> getTroubleTicketById(String id, TroubleTicket.@Valid TicketTypeEnum type, Throwable throwable) {
        log.info("Request to get TroubleTicket for id {} ",id);
        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {

            log.debug("Error status 400 API Management BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    default ResponseEntity<List<TroubleTicket>> getTroubleTicketByMsisdn(String publicKey, TroubleTicket.@Valid TicketTypeEnum type, Throwable throwable) {
        log.info("Request to emergencyCredit for msisdn  {}", publicKey);
        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 400) {

            log.debug("Error status 400 API Management BAD REQUEST : {} ", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());

        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }
}
