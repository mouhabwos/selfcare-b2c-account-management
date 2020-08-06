package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

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

    private static String rejected = "Rejetée";
    private static String cancelled = "Annulée";
    private static String acknowledgedRequest = "Validée";
    private static String heldRequest = "Réalisable";
    private static String inProgress = "En Cours";
    private static String heldIncident = "Signalée";
    private static String pending = "ABSENTAVIS";
    private static String acknowwledgedIncident = "Orienté";

    private static String rejectedRequestDescription = "Votre demande a été suspendue. Pour plus d’informations, merci de vous rapprocher du service client";
    private static String rejectedIncidentDescription = "Votre dérangement n'a pas abouti. Veuillez vous rapprocher du Service Client";
    private static String cancelledDescription = "Votre demande a été annulée pour non faisabilité technique ou sur demande du client";
    private static String acknowledgedRequestDescription = "Votre demande a été validée, vous serez contacté prochainement par nos équipes";
    private static String heldRequestDescription = "Votre demande est réalisable. Pour la valider vous serez invité à signer le contrat et payer les frais";
    private static String inProgressRequestDescription = "Votre demande est en cours de traitement, vous serez contacter par nos équipes.";
    private static String inProgressIncidentDescription = "Votre dérangement est en cours de traitement, vous serez contacter par nos équipes.";
    private static String heldIncidentDescription = "Votre dérangement est en cours et est pris charge, vous serez contacter par nos équipes.";
    private static String pendingDescription = "Nous n’avons réussi à vous joindre et/ou nous vous avons proposé un RDV pour vous joindre à nouveau";
    private static String acknowwledgedIncidentDescription = "Votre dérangement a été envoyée aux équipes techniques";

    private static String firstPosition = "1";
    private static String secondPosition = "2";
    private static String thirdPosition = "3";

    private static String trueValue = "true";
    private static String falseValue = "false";

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

    private RequestStatusDTO mapTroubleTicketToRequestStatus(TroubleTicket troubleTicket){

        RequestStatusDTO requestStatusDTO = new RequestStatusDTO();

        if(troubleTicket.getTicketType().equals(TroubleTicket.TicketTypeEnum.REQUEST.toString())){
            requestStatusDTO.setType(TroubleTicket.TicketTypeEnum.REQUEST);
            requestStatusDTO.setStatus(troubleTicket.getStatus());
            switch (troubleTicket.getStatus()){
                case "REJECTED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().getOrDefault(KEY_REJECTED_TITLE,rejected));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().getOrDefault(KEY_REJECTED_REQUEST_DESCRIPTION, rejectedRequestDescription));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().getOrDefault(KEY_HISTORIC_FALSE, falseValue)));
                    break;
                case "CANCELLED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().getOrDefault(KEY_CANCELLED_TITLE,cancelled));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().getOrDefault(KEY_CANCELLED_DESCRIPTION,cancelledDescription));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().getOrDefault(KEY_HISTORIC_FALSE, falseValue)));
                    break;
                case "ACKNOWLEDGED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().getOrDefault(KEY_ACKNOWLEDGED_REQUEST_TITLE, acknowledgedRequest));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().getOrDefault(KEY_HISTORIC_TRUE, trueValue)));
                    requestStatusDTO.setOrder(Integer.parseInt(applicationProperties.getOrder().getOrDefault(KEY_ORDER_THIRD, thirdPosition)));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().getOrDefault(KEY_ACKNOWLEDGED_REQUEST_DESCRIPTION,acknowledgedRequestDescription));
                    break;
                case "HELD":
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().getOrDefault(KEY_HELD_REQUEST_DESCRIPTION,heldRequestDescription));
                    requestStatusDTO.setOrder(Integer.parseInt(applicationProperties.getOrder().getOrDefault(KEY_ORDER_SECOND, secondPosition)));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().getOrDefault(KEY_HELD_REQUEST_TITLE, heldRequest));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().getOrDefault(KEY_HISTORIC_TRUE, trueValue)));
                    break;
                case "INPROGRESS":
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().getOrDefault(KEY_HISTORIC_TRUE, trueValue)));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().getOrDefault(KEY_IN_PROGRESS_REQUEST_DESCRIPTION,inProgressRequestDescription));
                    requestStatusDTO.setOrder(Integer.parseInt(applicationProperties.getOrder().getOrDefault(KEY_ORDER_FIRST, firstPosition)));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().getOrDefault(KEY_IN_PROGRESS_TITLE, inProgress));
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
                    requestStatusDTO.setOrder(Integer.parseInt(applicationProperties.getOrder().getOrDefault(KEY_ORDER_SECOND, secondPosition)));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().getOrDefault(KEY_HISTORIC_TRUE, trueValue)));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().getOrDefault(KEY_HELD_INCIDENT_TITLE, heldIncident));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().getOrDefault(KEY_HELD_INCIDENT_DESCRIPTION,heldIncidentDescription));
                    break;
                case "PENDING":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().getOrDefault(KEY_PENDING_TITLE,pending));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().getOrDefault(KEY_PENDING_DESCRIPTION,pendingDescription));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().getOrDefault(KEY_HISTORIC_FALSE, falseValue)));
                    break;
                case "INPROGRESS":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().getOrDefault(KEY_IN_PROGRESS_TITLE, inProgress));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().getOrDefault(KEY_HISTORIC_TRUE, trueValue)));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().getOrDefault(KEY_IN_PROGRESS_INCIDENT_DESCRIPTION,inProgressIncidentDescription));
                    requestStatusDTO.setOrder(Integer.parseInt(applicationProperties.getOrder().getOrDefault(KEY_ORDER_FIRST, firstPosition)));
                    break;
                case "ACKNOWLEDGED":
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().getOrDefault(KEY_ACKNOWLEDGED_INCIDENT_TITLE, acknowwledgedIncident));
                    requestStatusDTO.setOrder(Integer.parseInt(applicationProperties.getOrder().getOrDefault(KEY_ORDER_THIRD, thirdPosition)));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().getOrDefault(KEY_ACKNOWLEDGED_INCIDENT_DESCRIPTION, acknowwledgedIncidentDescription));
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().getOrDefault(KEY_HISTORIC_TRUE, trueValue)));
                    break;
                case "REJECTED":
                    requestStatusDTO.setHistoric(Boolean.parseBoolean(applicationProperties.getHistoric().getOrDefault(KEY_HISTORIC_FALSE, falseValue)));
                    requestStatusDTO.setTitle(applicationProperties.getRequestTitleMap().getOrDefault(KEY_REJECTED_TITLE,rejected));
                    requestStatusDTO.setDescription(applicationProperties.getRequestDescriptionMap().getOrDefault(KEY_REJECTED_INCIDENT_DESCRIPTION,rejectedIncidentDescription));
                    break;
                default:
                    requestStatusDTO.setTitle("INCONNNU");
                    break;
            }
        }
        return requestStatusDTO;
    }
}


