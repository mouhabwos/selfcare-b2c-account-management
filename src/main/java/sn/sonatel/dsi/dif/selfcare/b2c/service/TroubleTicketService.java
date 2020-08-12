package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RequestStatusDTO;

import java.util.List;

/**
 * @author Sogui
 * @since 1.9.2
 */
public interface TroubleTicketService {

    ResponseEntity<List<RequestStatusDTO>> getRequestStatusById(String id);

    ResponseEntity<List<RequestStatusDTO>> getRequestStatusByMisisdn(String msisdn);
}
