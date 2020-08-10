package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.service.TroubleTicketService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.TroubleTicketApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.TroubleTicket;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RequestStatusDTO;

import java.util.LinkedList;
import java.util.List;

@Service
public class TroubletIcketServiceImpl implements TroubleTicketService {

    private final TroubleTicketApiClient  troubleTicketApiClient;

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
    public ResponseEntity<RequestStatusDTO> getRequestStatusById(String id, TroubleTicket.TicketTypeEnum type) {

        ResponseEntity<TroubleTicket> responseEntity = troubleTicketApiClient.getTroubleTicketById(id, type);
        RequestStatusDTO result;
        if(responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null){
            result = mapTroubleTicketToRequestStatus(responseEntity.getBody());
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @Override
    public ResponseEntity<List<RequestStatusDTO>> getRequestStatusByMisisdn(String msisdn, TroubleTicket.TicketTypeEnum type) {

        ResponseEntity<List<TroubleTicket>> responseEntity = troubleTicketApiClient.getTroubleTicketByMsisdn(msisdn, type);
        List<RequestStatusDTO> resultList = new LinkedList<>();
        if(responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null){
            for(TroubleTicket troubleTicket : responseEntity.getBody()){
                RequestStatusDTO requestStatusDTO = mapTroubleTicketToRequestStatus(troubleTicket);
                resultList.add(requestStatusDTO);
            }
            return ResponseEntity.ok(resultList);
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    private RequestStatusDTO mapTroubleTicketToRequestStatus(TroubleTicket troubleTicket){

        RequestStatusDTO requestStatusDTO = new RequestStatusDTO();
        requestStatusDTO.setRequestId(troubleTicket.getId());

        if(troubleTicket.getTicketType().equals(TroubleTicket.TicketTypeEnum.REQUEST.toString())){
            requestStatusDTO.setType(TroubleTicket.TicketTypeEnum.REQUEST);
            requestStatusDTO.setStatus(troubleTicket.getStatus());
            switch (troubleTicket.getStatus()){
                case "REJECTED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_REJECTED_TITLE));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_REJECTED_REQUEST_DESCRIPTION));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_FALSE)));
                    break;
                case "CANCELLED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_CANCELLED_TITLE));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_CANCELLED_DESCRIPTION));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_FALSE)));
                    break;
                case "ACKNOWLEDGED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_ACKNOWLEDGED_REQUEST_TITLE));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    requestStatusDTO.setOrder(applicationProperties.getOrder().get(KEY_ORDER_THIRD));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_ACKNOWLEDGED_REQUEST_DESCRIPTION));
                    break;
                case "HELD":
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_HELD_REQUEST_DESCRIPTION));
                    requestStatusDTO.setOrder(applicationProperties.getOrder().get(KEY_ORDER_SECOND));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_HELD_REQUEST_TITLE));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    break;
                case "INPROGRESS":
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_IN_PROGRESS_REQUEST_DESCRIPTION));
                    requestStatusDTO.setOrder(applicationProperties.getOrder().get(KEY_ORDER_FIRST));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_IN_PROGRESS_TITLE));
                    break;
                default:
                    requestStatusDTO.setTitle("INCONNNU");
                    break;
            }
        }
        if(troubleTicket.getTicketType().equals(TroubleTicket.TicketTypeEnum.INCIDENT.toString())){
            requestStatusDTO.setType(TroubleTicket.TicketTypeEnum.INCIDENT);
            requestStatusDTO.setStatus(troubleTicket.getStatus());
            switch (troubleTicket.getStatus()){
                case "HELD":
                    requestStatusDTO.setOrder(applicationProperties.getOrder().get(KEY_ORDER_SECOND));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_HELD_INCIDENT_TITLE));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_HELD_INCIDENT_DESCRIPTION));
                    break;
                case "PENDING":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_PENDING_TITLE));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_PENDING_DESCRIPTION));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_FALSE)));
                    break;
                case "INPROGRESS":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_IN_PROGRESS_TITLE));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_IN_PROGRESS_INCIDENT_DESCRIPTION));
                    requestStatusDTO.setOrder((applicationProperties.getOrder().get(KEY_ORDER_FIRST)));
                    break;
                case "ACKNOWLEDGED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_ACKNOWLEDGED_INCIDENT_TITLE));
                    requestStatusDTO.setOrder(applicationProperties.getOrder().get(KEY_ORDER_THIRD));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_ACKNOWLEDGED_INCIDENT_DESCRIPTION));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_TRUE)));
                    break;
                case "REJECTED":
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().get(KEY_HISTORIC_FALSE)));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().get(KEY_REJECTED_TITLE));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().get(KEY_REJECTED_INCIDENT_DESCRIPTION));
                    break;
                default:
                    requestStatusDTO.setTitle("INCONNNU");
                    break;
            }
        }
        return requestStatusDTO;
    }
}


