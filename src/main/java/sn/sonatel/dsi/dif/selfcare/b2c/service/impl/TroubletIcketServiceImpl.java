package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.service.TroubleTicketService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.TroubleTicketApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.TroubleTicket;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RequestStatusDTO;

@Service
public class TroubletIcketServiceImpl implements TroubleTicketService {

    private final TroubleTicketApiClient troubleTicketApiClient;

    private final ApplicationProperties applicationProperties;

    private static final String KEY_REJECTED_TITLE = "rejected-title";
    private static final String KEY_CANCELLED_TITLE = "cancelled-title";
    private static final String KEY_ACKNOWLEDGED_REQUEST_TITLE = "acknowledged-request-title";
    private static final String KEY_HELD_REQUEST_TITLE = "held-request-title";
    private static final String KEY_IN_PROGRESS_TITLE = "in-progress-title";
    private static final String KEY_HELD_INCIDENT_TITLE = "held-incident-title";
    private static final String KEY_PENDING_TITLE = "pending-title";
    private static final String KEY_ACKNOWLEDGED_INCIDENT_TITLE = "acknowledged-incident-title";

    private static final String KEY_REJECTED_REQUEST_DESCRIPTION = "rejected-request-description";
    private static final String KEY_REJECTED_INCIDENT_DESCRIPTION = "rejected-incident-description";
    private static final String KEY_CANCELLED_DESCRIPTION = "cancelled-description";
    private static final String KEY_ACKNOWLEDGED_REQUEST_DESCRIPTION = "acknowledged-request-description";
    private static final String KEY_HELD_REQUEST_DESCRIPTION = "held-request-description";
    private static final String KEY_IN_PROGRESS_REQUEST_DESCRIPTION = "in-progress-request-description";
    private static final String KEY_IN_PROGRESS_INCIDENT_DESCRIPTION = "in-progress-incident-description";
    private static final String KEY_HELD_INCIDENT_DESCRIPTION = "held-incident-description";
    private static final String KEY_PENDING_DESCRIPTION = "pending-description";
    private static final String KEY_ACKNOWLEDGED_INCIDENT_DESCRIPTION = "acknowledged-incident-description";

    private static final String KEY_ORDER_FIRST = "first";
    private static final String KEY_ORDER_SECOND = "second";
    private static final String KEY_ORDER_THIRD = "third";

    private static final String KEY_HISTORIC_TRUE = "positive";
    private static final String KEY_HISTORIC_FALSE = "negative";

    public TroubletIcketServiceImpl(TroubleTicketApiClient troubleTicketApiClient, ApplicationProperties applicationProperties) {
        this.troubleTicketApiClient = troubleTicketApiClient;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public ResponseEntity<List<RequestStatusDTO>> getRequestStatusById(String id) {
        ResponseEntity<TroubleTicket> troubleRequest = troubleTicketApiClient.getTroubleTicketById(
            id,
            TroubleTicket.TicketTypeEnum.REQUEST
        );
        ResponseEntity<TroubleTicket> troubleIncdent = troubleTicketApiClient.getTroubleTicketById(
            id,
            TroubleTicket.TicketTypeEnum.INCIDENT
        );
        RequestStatusDTO result;
        TroubleTicket troubleRequestBody = troubleRequest.getBody();
        if (troubleRequest.getStatusCode() == HttpStatus.OK && troubleRequestBody != null) {
            result = mapTroubleTicketToRequestStatus(troubleRequestBody);
            if (Boolean.TRUE.equals(result.getHistoric())) {
                List<RequestStatusDTO> resultList = orderRequest(result);
                resultList.forEach(e -> e.setRequestId(result.getRequestId()));
                return ResponseEntity.ok(resultList);
            } else {
                List<RequestStatusDTO> requestStatusDTOList = new LinkedList<>();
                requestStatusDTOList.add(result);
                return ResponseEntity.ok(requestStatusDTOList);
            }
        }
        TroubleTicket troubleIncdentBody = troubleIncdent.getBody();
        if (troubleIncdent.getStatusCode() == HttpStatus.OK && troubleIncdentBody != null) {
            result = mapTroubleTicketToRequestStatus(troubleIncdentBody);
            if (Boolean.TRUE.equals(result.getHistoric())) {
                List<RequestStatusDTO> resultList = orderRequest(result);
                resultList.forEach(e -> e.setRequestId(result.getRequestId()));
                return ResponseEntity.ok(resultList);
            } else {
                List<RequestStatusDTO> requestStatusDTOList = new LinkedList<>();
                requestStatusDTOList.add(result);
                return ResponseEntity.ok(requestStatusDTOList);
            }
        }

        return ResponseEntity.ok(Collections.emptyList());
    }

    @Override
    public ResponseEntity<List<RequestStatusDTO>> getRequestStatusByMisisdn(String msisdn) {
        ResponseEntity<List<TroubleTicket>> responseRequest = troubleTicketApiClient.getTroubleTicketByMsisdn(
            msisdn,
            TroubleTicket.TicketTypeEnum.REQUEST
        );
        ResponseEntity<List<TroubleTicket>> responseIncident = troubleTicketApiClient.getTroubleTicketByMsisdn(
            msisdn,
            TroubleTicket.TicketTypeEnum.INCIDENT
        );

        List<TroubleTicket> troubleTicketList = new LinkedList<>();
        List<RequestStatusDTO> resultList = new LinkedList<>();
        if (responseRequest.getStatusCode() == HttpStatus.OK && responseRequest.getBody() != null) {
            troubleTicketList.addAll(responseRequest.getBody());
        }
        if (responseIncident.getStatusCode() == HttpStatus.OK && responseIncident.getBody() != null) {
            troubleTicketList.addAll(responseIncident.getBody());
        }
        for (TroubleTicket troubleTicket : troubleTicketList) {
            RequestStatusDTO requestStatusDTO = mapTroubleTicketToRequestStatus(troubleTicket);
            requestStatusDTO.setCurrentState(true);
            resultList.add(requestStatusDTO);
        }
        return ResponseEntity.ok(resultList);
    }

    public List<RequestStatusDTO> orderRequest(RequestStatusDTO requestStatusDTO) {
        List<RequestStatusDTO> requestStatusDTOList = getAllRequestsOrderedBYType(requestStatusDTO.getType());
        requestStatusDTO.setCurrentState(true);
        boolean gotOrder = false;
        for (Iterator<RequestStatusDTO> iterator = requestStatusDTOList.iterator(); iterator.hasNext();) {
            if (iterator.next().getOrder() == requestStatusDTO.getOrder()) {
                iterator.remove();
                gotOrder = true;
            }
        }
        if (gotOrder) {
            requestStatusDTOList.add(requestStatusDTO);
        }

        return requestStatusDTOList;
    }

    public List<RequestStatusDTO> getAllRequestsOrderedBYType(TroubleTicket.TicketTypeEnum typeEnum) {
        List<RequestStatusDTO> resultList = new LinkedList<>();
        if (typeEnum == TroubleTicket.TicketTypeEnum.REQUEST) {
            RequestStatusDTO requestStatusDTO1 = new RequestStatusDTO();
            requestStatusDTO1.setTitle(applicationProperties.getRequestTitleMap().get(KEY_IN_PROGRESS_TITLE));
            requestStatusDTO1.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_IN_PROGRESS_REQUEST_DESCRIPTION));
            requestStatusDTO1.setType(TroubleTicket.TicketTypeEnum.REQUEST);
            requestStatusDTO1.setStatus(TroubleTicket.StatusEnum.INPROGRESS.value());
            requestStatusDTO1.setOrder(applicationProperties.getOrder().get(KEY_ORDER_SECOND));
            resultList.add(requestStatusDTO1);

            RequestStatusDTO requestStatusDTO2 = new RequestStatusDTO();
            requestStatusDTO2.setTitle(applicationProperties.getRequestTitleMap().get(KEY_HELD_REQUEST_TITLE));
            requestStatusDTO2.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_HELD_REQUEST_DESCRIPTION));
            requestStatusDTO2.setType(TroubleTicket.TicketTypeEnum.REQUEST);
            requestStatusDTO2.setStatus(TroubleTicket.StatusEnum.HELD.value());
            requestStatusDTO2.setOrder(applicationProperties.getOrder().get(KEY_ORDER_THIRD));
            resultList.add(requestStatusDTO2);

            RequestStatusDTO requestStatusDTO3 = new RequestStatusDTO();
            requestStatusDTO3.setTitle(applicationProperties.getRequestTitleMap().get(KEY_ACKNOWLEDGED_REQUEST_TITLE));
            requestStatusDTO3.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_ACKNOWLEDGED_REQUEST_DESCRIPTION));
            requestStatusDTO3.setType(TroubleTicket.TicketTypeEnum.REQUEST);
            requestStatusDTO3.setStatus(TroubleTicket.StatusEnum.ACKNOWLEDGED.value());
            requestStatusDTO3.setOrder(applicationProperties.getOrder().get(KEY_ORDER_FIRST));
            resultList.add(requestStatusDTO3);
        }
        if (typeEnum == TroubleTicket.TicketTypeEnum.INCIDENT) {
            RequestStatusDTO requestStatusDTO1 = new RequestStatusDTO();
            requestStatusDTO1.setTitle(applicationProperties.getRequestTitleMap().get(KEY_IN_PROGRESS_TITLE));
            requestStatusDTO1.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_IN_PROGRESS_INCIDENT_DESCRIPTION));
            requestStatusDTO1.setType(TroubleTicket.TicketTypeEnum.INCIDENT);
            requestStatusDTO1.setStatus(TroubleTicket.StatusEnum.INPROGRESS.value());
            requestStatusDTO1.setOrder(applicationProperties.getOrder().get(KEY_ORDER_SECOND));
            resultList.add(requestStatusDTO1);

            RequestStatusDTO requestStatusDTO2 = new RequestStatusDTO();
            requestStatusDTO2.setTitle(applicationProperties.getRequestTitleMap().get(KEY_HELD_INCIDENT_TITLE));
            requestStatusDTO2.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_HELD_INCIDENT_DESCRIPTION));
            requestStatusDTO2.setType(TroubleTicket.TicketTypeEnum.INCIDENT);
            requestStatusDTO2.setStatus(TroubleTicket.StatusEnum.HELD.value());
            requestStatusDTO2.setOrder(applicationProperties.getOrder().get(KEY_ORDER_THIRD));
            resultList.add(requestStatusDTO2);

            RequestStatusDTO requestStatusDTO3 = new RequestStatusDTO();
            requestStatusDTO3.setTitle(applicationProperties.getRequestTitleMap().get(KEY_ACKNOWLEDGED_INCIDENT_TITLE));
            requestStatusDTO3.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_ACKNOWLEDGED_INCIDENT_DESCRIPTION));
            requestStatusDTO3.setType(TroubleTicket.TicketTypeEnum.INCIDENT);
            requestStatusDTO3.setStatus(TroubleTicket.StatusEnum.ACKNOWLEDGED.value());
            requestStatusDTO3.setOrder(applicationProperties.getOrder().get(KEY_ORDER_FIRST));
            resultList.add(requestStatusDTO3);
        }

        return resultList;
    }

    private RequestStatusDTO mapTroubleTicketToRequestStatus(TroubleTicket troubleTicket) {
        RequestStatusDTO requestStatusDTO = new RequestStatusDTO();
        requestStatusDTO.setRequestId(troubleTicket.getId());

        if (troubleTicket.getTicketType().equals(TroubleTicket.TicketTypeEnum.REQUEST.toString())) {
            requestStatusDTO.setType(TroubleTicket.TicketTypeEnum.REQUEST);
            requestStatusDTO.setStatus(troubleTicket.getStatus());
            switch (troubleTicket.getStatus()) {
                case "REJECTED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_REJECTED_TITLE));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_REJECTED_REQUEST_DESCRIPTION));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_FALSE)));
                    requestStatusDTO.setCurrentState(true);
                    break;
                case "CANCELLED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_CANCELLED_TITLE));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_CANCELLED_DESCRIPTION));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_FALSE)));
                    requestStatusDTO.setCurrentState(true);
                    break;
                case "ACKNOWLEDGED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_ACKNOWLEDGED_REQUEST_TITLE));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    requestStatusDTO.setOrder(applicationProperties.getOrder().get(KEY_ORDER_FIRST));
                    requestStatusDTO.setDescription(
                        applicationProperties.getRequestDescriptionMap().get(KEY_ACKNOWLEDGED_REQUEST_DESCRIPTION)
                    );
                    break;
                case "HELD":
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_HELD_REQUEST_DESCRIPTION));
                    requestStatusDTO.setOrder(applicationProperties.getOrder().get(KEY_ORDER_THIRD));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_HELD_REQUEST_TITLE));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    break;
                case "INPROGRESS":
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    requestStatusDTO.setDescription(
                        applicationProperties.getRequestDescriptionMap().get(KEY_IN_PROGRESS_REQUEST_DESCRIPTION)
                    );
                    requestStatusDTO.setOrder(applicationProperties.getOrder().get(KEY_ORDER_SECOND));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_IN_PROGRESS_TITLE));
                    break;
                default:
                    requestStatusDTO.setTitle("INCONNNU");
                    break;
            }
        }
        if (troubleTicket.getTicketType().equals(TroubleTicket.TicketTypeEnum.INCIDENT.toString())) {
            requestStatusDTO.setType(TroubleTicket.TicketTypeEnum.INCIDENT);
            requestStatusDTO.setStatus(troubleTicket.getStatus());
            switch (troubleTicket.getStatus()) {
                case "HELD":
                    requestStatusDTO.setOrder(applicationProperties.getOrder().get(KEY_ORDER_THIRD));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_HELD_INCIDENT_TITLE));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_HELD_INCIDENT_DESCRIPTION));
                    break;
                case "PENDING":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_PENDING_TITLE));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_PENDING_DESCRIPTION));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_FALSE)));
                    requestStatusDTO.setCurrentState(true);
                    break;
                case "INPROGRESS":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_IN_PROGRESS_TITLE));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    requestStatusDTO.setDescription(
                        applicationProperties.getRequestDescriptionMap().get(KEY_IN_PROGRESS_INCIDENT_DESCRIPTION)
                    );
                    requestStatusDTO.setOrder((applicationProperties.getOrder().get(KEY_ORDER_SECOND)));
                    break;
                case "ACKNOWLEDGED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_ACKNOWLEDGED_INCIDENT_TITLE));
                    requestStatusDTO.setOrder(applicationProperties.getOrder().get(KEY_ORDER_FIRST));
                    requestStatusDTO.setDescription(
                        applicationProperties.getRequestDescriptionMap().get(KEY_ACKNOWLEDGED_INCIDENT_DESCRIPTION)
                    );
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    break;
                case "REJECTED":
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_FALSE)));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_REJECTED_TITLE));
                    requestStatusDTO.setDescription(
                        applicationProperties.getRequestDescriptionMap().get(KEY_REJECTED_INCIDENT_DESCRIPTION)
                    );
                    requestStatusDTO.setCurrentState(true);
                    break;
                default:
                    requestStatusDTO.setTitle("INCONNNU");
                    break;
            }
        }
        return requestStatusDTO;
    }
}
