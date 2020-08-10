package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.TroubleTicket;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RequestStatusDTO;

import java.util.List;

/**
 * @author Sogui
 * @since 1.9.2
 */
public interface TroubleTicketService {

    ResponseEntity<RequestStatusDTO> getRequestStatusById(String id, TroubleTicket.TicketTypeEnum type);

    ResponseEntity<List<RequestStatusDTO>> getRequestStatusByMisisdn(String msisdn, TroubleTicket.TicketTypeEnum type);
}
