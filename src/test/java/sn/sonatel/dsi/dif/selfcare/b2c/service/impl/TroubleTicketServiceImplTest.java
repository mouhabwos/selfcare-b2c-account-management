package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.service.TroubleTicketService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.TroubleTicketApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.TroubleTicket;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RequestStatusDTO;

import java.util.*;

import static org.mockito.MockitoAnnotations.initMocks;

public class TroubleTicketServiceImplTest {

    @Mock
    TroubleTicketApiClient troubleTicketApiClient;

    @Mock
    ApplicationProperties applicationProperties;

    private TroubleTicketService troubleTicketService;

    private final static String REQUEST_ID = "51022830";
    private final static String REQUEST_MSISDN = "338239614";

    static Map<String, String> titleMap = new HashMap<>();
    static Map<String, String> descriptionMap = new HashMap<>();
    static Map<String, String> historicMap = new HashMap<>();
    static Map<String, Integer> orderMap = new HashMap<>();

    @Before
    public void setUp() {
        initMocks(this);
        troubleTicketService = new TroubletIcketServiceImpl(troubleTicketApiClient, applicationProperties);
    }

   static  {
        titleMap.put("rejected-title","Rejetée");
        titleMap.put("cancelled-title","Annulée");
        titleMap.put("acknowledged-request-title","Validée");
        titleMap.put("held-request-title","Réalisable");
        titleMap.put("in-progress-title","En Cours");
        titleMap.put("held-incident-title","Signalée");
        titleMap.put("pending-title","ABSENTAVIS");
        titleMap.put("acknowledged-incident-title","Orienté");

        descriptionMap.put("rejected-request-description","Votre demande a été suspendue. Pour plus d’informations, merci de vous rapprocher du service client");
        descriptionMap.put("cancelled-description","Votre demande a été annulée pour non faisabilité technique ou sur demande du client");
        descriptionMap.put("acknowledged-request-description","Votre demande a été validée, vous serez contacté prochainement par nos équipes");
        descriptionMap.put("held-request-description","Votre demande est réalisable. Pour la valider vous serez invité à signer le contrat et payer les frais");
        descriptionMap.put("in-progress-request-description","Votre demande est en cours de traitement, vous serez contacter par nos équipes.");
        descriptionMap.put("held-incident-description","Votre dérangement est en cours et est pris charge, vous serez contacter par nos équipes.");
        descriptionMap.put("pending-description","Nous n’avons réussi à vous joindre et/ou nous vous avons proposé un RDV pour vous joindre à nouveau");
        descriptionMap.put("in-progress-incident-description","Votre dérangement est en cours de traitement, vous serez contacter par nos équipes.");
        descriptionMap.put("acknowledged-incident-description","Votre dérangement a été envoyée aux équipes techniques");
        descriptionMap.put("rejected-incident-description","Votre dérangement n'a pas abouti. Veuillez vous rapprocher du Service Client");

        historicMap.put("positive","true");
        historicMap.put("negative","false");

        orderMap.put("first",1);
        orderMap.put("second",2);
        orderMap.put("third",3);

    }

    @Test
    public void getRequestStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.INPROGRESS);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicket));
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(3, responseEntity.getBody().size());
        Assert.assertEquals("INPROGRESS", Objects.requireNonNull(responseEntity.getBody().get(2)).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(2).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().get(2).getType());
        Assert.assertEquals("Votre demande est en cours de traitement, vous serez contacter par nos équipes.", responseEntity.getBody().get(2).getDescription());
        Assert.assertEquals("En Cours", responseEntity.getBody().get(2).getTitle());
        Assert.assertEquals(1, responseEntity.getBody().get(2).getOrder());
        Assert.assertEquals(true, responseEntity.getBody().get(2).getHistoric());

    }

    @Test
    public void getRequestCancelledStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.CANCELLED);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicket));
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>>responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(1, responseEntity.getBody().size());
        Assert.assertEquals("CANCELLED", responseEntity.getBody().get(0).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(0).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().get(0).getType());
        Assert.assertEquals("Votre demande a été annulée pour non faisabilité technique ou sur demande du client", responseEntity.getBody().get(0).getDescription());
        Assert.assertEquals("Annulée", responseEntity.getBody().get(0).getTitle());
        Assert.assertEquals(false, responseEntity.getBody().get(0).getHistoric());

    }

    @Test
    public void getRequestRejectedStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.REJECTED);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicket));
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(1, responseEntity.getBody().size());
        Assert.assertEquals("REJECTED", responseEntity.getBody().get(0).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(0).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().get(0).getType());
        Assert.assertEquals("Votre demande a été suspendue. Pour plus d’informations, merci de vous rapprocher du service client", responseEntity.getBody().get(0).getDescription());
        Assert.assertEquals("Rejetée", responseEntity.getBody().get(0).getTitle());
        Assert.assertEquals(false, responseEntity.getBody().get(0).getHistoric());

    }
    @Test
    public void getRequestAcknowledgedStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.ACKNOWLEDGED);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicket));
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(3, responseEntity.getBody().size());
        Assert.assertEquals("ACKNOWLEDGED", responseEntity.getBody().get(2).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(2).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().get(2).getType());
        Assert.assertEquals("Votre demande a été validée, vous serez contacté prochainement par nos équipes", responseEntity.getBody().get(2).getDescription());
        Assert.assertEquals("Validée", responseEntity.getBody().get(2).getTitle());
        Assert.assertEquals(true, responseEntity.getBody().get(2).getHistoric());
        Assert.assertEquals(3, responseEntity.getBody().get(2).getOrder());

    }

    @Test
    public void getRequestHeldStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.HELD);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicket));
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(3, responseEntity.getBody().size());
        Assert.assertEquals("HELD", responseEntity.getBody().get(2).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(2).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().get(2).getType());
        Assert.assertEquals("Votre demande est réalisable. Pour la valider vous serez invité à signer le contrat et payer les frais", responseEntity.getBody().get(2).getDescription());
        Assert.assertEquals("Réalisable", responseEntity.getBody().get(2).getTitle());
        Assert.assertEquals(true, responseEntity.getBody().get(2).getHistoric());
        Assert.assertEquals(2, responseEntity.getBody().get(2).getOrder());

    }

    @Test
    public void getIncidentStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.INCIDENT);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.ACKNOWLEDGED);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(troubleTicket));
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(3, responseEntity.getBody().size());
        Assert.assertEquals("ACKNOWLEDGED", responseEntity.getBody().get(2).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(2).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.INCIDENT, responseEntity.getBody().get(2).getType());
        Assert.assertEquals("Votre dérangement a été envoyée aux équipes techniques", responseEntity.getBody().get(2).getDescription());
        Assert.assertEquals("Orienté", responseEntity.getBody().get(2).getTitle());
        Assert.assertEquals(3, responseEntity.getBody().get(2).getOrder());
        Assert.assertEquals(true, responseEntity.getBody().get(2).getHistoric());

    }

    @Test
    public void getIncidentHeldStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.INCIDENT);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.HELD);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(troubleTicket));
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(3, responseEntity.getBody().size());
        Assert.assertEquals("HELD", responseEntity.getBody().get(2).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(2).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.INCIDENT, responseEntity.getBody().get(2).getType());
        Assert.assertEquals("Votre dérangement est en cours et est pris charge, vous serez contacter par nos équipes.", responseEntity.getBody().get(2).getDescription());
        Assert.assertEquals("Signalée", responseEntity.getBody().get(2).getTitle());
        Assert.assertEquals(2, responseEntity.getBody().get(2).getOrder());
        Assert.assertEquals(true, responseEntity.getBody().get(2).getHistoric());

    }

    @Test
    public void getIncidentProgressStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.INCIDENT);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.INPROGRESS);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(troubleTicket));
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(3, responseEntity.getBody().size());
        Assert.assertEquals("INPROGRESS", responseEntity.getBody().get(2).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(2).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.INCIDENT, responseEntity.getBody().get(2).getType());
        Assert.assertEquals("Votre dérangement est en cours de traitement, vous serez contacter par nos équipes.", responseEntity.getBody().get(2).getDescription());
        Assert.assertEquals("En Cours", responseEntity.getBody().get(2).getTitle());
        Assert.assertEquals(1, responseEntity.getBody().get(2).getOrder());
        Assert.assertEquals(true, responseEntity.getBody().get(2).getHistoric());

    }

    @Test
    public void getIncidentRejectedStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.INCIDENT);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.REJECTED);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(troubleTicket));
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(1, responseEntity.getBody().size());
        Assert.assertEquals("REJECTED", responseEntity.getBody().get(0).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(0).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.INCIDENT, responseEntity.getBody().get(0).getType());
        Assert.assertEquals("Votre dérangement n'a pas abouti. Veuillez vous rapprocher du Service Client", responseEntity.getBody().get(0).getDescription());
        Assert.assertEquals("Rejetée", responseEntity.getBody().get(0).getTitle());
        Assert.assertEquals(false, responseEntity.getBody().get(0).getHistoric());

    }

    @Test
    public void getIncidentPendingStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.INCIDENT);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.PENDING);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(troubleTicket));
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(1, responseEntity.getBody().size());
        Assert.assertEquals("PENDING", responseEntity.getBody().get(0).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(0).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.INCIDENT, responseEntity.getBody().get(0).getType());
        Assert.assertEquals("Nous n’avons réussi à vous joindre et/ou nous vous avons proposé un RDV pour vous joindre à nouveau", responseEntity.getBody().get(0).getDescription());
        Assert.assertEquals("ABSENTAVIS", responseEntity.getBody().get(0).getTitle());
        Assert.assertEquals(false, responseEntity.getBody().get(0).getHistoric());

    }

    @Test
    public void getRequestStatusForWrongId(){

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.notFound().build());
        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID);

        Assert.assertEquals(Collections.emptyList(), responseEntity.getBody());
    }

    @Test
    public void getRequestStatusByMsisdn(){

        List<TroubleTicket> troubleTicketList = new LinkedList<>();

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.INPROGRESS);
        troubleTicket.setId(REQUEST_ID);

        Mockito.when(applicationProperties.getHistoric()).thenReturn(historicMap);
        Mockito.when(applicationProperties.getOrder()).thenReturn(orderMap);
        Mockito.when(applicationProperties.getRequestDescriptionMap()).thenReturn(descriptionMap);
        Mockito.when(applicationProperties.getRequestTitleMap()).thenReturn(titleMap);

        troubleTicketList.add(troubleTicket);

        Mockito.when(troubleTicketApiClient.getTroubleTicketByMsisdn(REQUEST_MSISDN,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicketList));

        Mockito.when(troubleTicketApiClient.getTroubleTicketByMsisdn(REQUEST_MSISDN,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(Collections.EMPTY_LIST));

        ResponseEntity<List<RequestStatusDTO>> responseEntity = troubleTicketService.getRequestStatusByMisisdn(REQUEST_MSISDN);

        Assert.assertEquals("INPROGRESS", Objects.requireNonNull(responseEntity.getBody()).get(0).getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().get(0).getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().get(0).getType());
        Assert.assertEquals("Votre demande est en cours de traitement, vous serez contacter par nos équipes.", responseEntity.getBody().get(0).getDescription());
        Assert.assertEquals("En Cours", responseEntity.getBody().get(0).getTitle());
        Assert.assertEquals(1, responseEntity.getBody().get(0).getOrder());
        Assert.assertEquals(true, responseEntity.getBody().get(0).getHistoric());

    }
}
