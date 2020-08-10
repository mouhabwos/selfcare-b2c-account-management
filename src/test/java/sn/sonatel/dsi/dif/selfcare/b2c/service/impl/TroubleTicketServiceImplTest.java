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

import static org.mockito.MockitoAnnotations.initMocks;

public class TroubleTicketServiceImplTest {

    @Mock
    TroubleTicketApiClient troubleTicketApiClient;

    @Mock
    ApplicationProperties applicationProperties;

    private TroubleTicketService troubleTicketService;

    private final static String REQUEST_ID = "51022830";

    @Before
    public void setUp() {
        initMocks(this);
        troubleTicketService = new TroubletIcketServiceImpl(troubleTicketApiClient, applicationProperties);
    }

    @Test
    public void getRequestStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.INPROGRESS);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicket));

        ResponseEntity<RequestStatusDTO> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST);

        Assert.assertEquals("INPROGRESS", responseEntity.getBody().getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().getType());
        Assert.assertEquals("Votre demande est en cours de traitement, vous serez contacter par nos équipes.", responseEntity.getBody().getDescription());
        Assert.assertEquals("En Cours", responseEntity.getBody().getTitle());
        Assert.assertEquals(1, responseEntity.getBody().getOrder());
        Assert.assertEquals(true, responseEntity.getBody().getHistoric());

    }

    @Test
    public void getRequestCancelledStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.CANCELLED);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicket));

        ResponseEntity<RequestStatusDTO> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST);

        Assert.assertEquals("CANCELLED", responseEntity.getBody().getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().getType());
        Assert.assertEquals("Votre demande a été annulée pour non faisabilité technique ou sur demande du client", responseEntity.getBody().getDescription());
        Assert.assertEquals("Annulée", responseEntity.getBody().getTitle());
        Assert.assertEquals(false, responseEntity.getBody().getHistoric());

    }

    @Test
    public void getRequestRejectedStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.REJECTED);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicket));

        ResponseEntity<RequestStatusDTO> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST);

        Assert.assertEquals("REJECTED", responseEntity.getBody().getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().getType());
        Assert.assertEquals("Votre demande a été suspendue. Pour plus d’informations, merci de vous rapprocher du service client", responseEntity.getBody().getDescription());
        Assert.assertEquals("Rejetée", responseEntity.getBody().getTitle());
        Assert.assertEquals(false, responseEntity.getBody().getHistoric());

    }
    @Test
    public void getRequestAcknowledgedStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.ACKNOWLEDGED);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicket));

        ResponseEntity<RequestStatusDTO> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST);

        Assert.assertEquals("ACKNOWLEDGED", responseEntity.getBody().getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().getType());
        Assert.assertEquals("Votre demande a été validée, vous serez contacté prochainement par nos équipes", responseEntity.getBody().getDescription());
        Assert.assertEquals("Validée", responseEntity.getBody().getTitle());
        Assert.assertEquals(true, responseEntity.getBody().getHistoric());
        Assert.assertEquals(3, responseEntity.getBody().getOrder());

    }

    @Test
    public void getRequestHeldStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.REQUEST);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.HELD);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST)).
            thenReturn(ResponseEntity.ok(troubleTicket));

        ResponseEntity<RequestStatusDTO> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID,TroubleTicket.TicketTypeEnum.REQUEST);

        Assert.assertEquals("HELD", responseEntity.getBody().getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.REQUEST, responseEntity.getBody().getType());
        Assert.assertEquals("Votre demande est réalisable. Pour la valider vous serez invité à signer le contrat et payer les frais", responseEntity.getBody().getDescription());
        Assert.assertEquals("Réalisable", responseEntity.getBody().getTitle());
        Assert.assertEquals(true, responseEntity.getBody().getHistoric());
        Assert.assertEquals(2, responseEntity.getBody().getOrder());

    }

    @Test
    public void getIncidentStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.INCIDENT);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.ACKNOWLEDGED);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(troubleTicket));

        ResponseEntity<RequestStatusDTO> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT);

        Assert.assertEquals("ACKNOWLEDGED", responseEntity.getBody().getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.INCIDENT, responseEntity.getBody().getType());
        Assert.assertEquals("Votre dérangement a été envoyée aux équipes techniques", responseEntity.getBody().getDescription());
        Assert.assertEquals("Orienté", responseEntity.getBody().getTitle());
        Assert.assertEquals(3, responseEntity.getBody().getOrder());
        Assert.assertEquals(true, responseEntity.getBody().getHistoric());

    }

    @Test
    public void getIncidentHeldStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.INCIDENT);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.HELD);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(troubleTicket));

        ResponseEntity<RequestStatusDTO> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT);

        Assert.assertEquals("HELD", responseEntity.getBody().getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.INCIDENT, responseEntity.getBody().getType());
        Assert.assertEquals("Votre dérangement est en cours et est pris charge, vous serez contacter par nos équipes.", responseEntity.getBody().getDescription());
        Assert.assertEquals("Signalée", responseEntity.getBody().getTitle());
        Assert.assertEquals(2, responseEntity.getBody().getOrder());
        Assert.assertEquals(true, responseEntity.getBody().getHistoric());

    }

    @Test
    public void getIncidentProgressStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.INCIDENT);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.INPROGRESS);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(troubleTicket));

        ResponseEntity<RequestStatusDTO> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT);

        Assert.assertEquals("INPROGRESS", responseEntity.getBody().getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.INCIDENT, responseEntity.getBody().getType());
        Assert.assertEquals("Votre dérangement est en cours de traitement, vous serez contacter par nos équipes.", responseEntity.getBody().getDescription());
        Assert.assertEquals("En Cours", responseEntity.getBody().getTitle());
        Assert.assertEquals(1, responseEntity.getBody().getOrder());
        Assert.assertEquals(true, responseEntity.getBody().getHistoric());

    }

    @Test
    public void getIncidentRejectedStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.INCIDENT);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.REJECTED);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(troubleTicket));

        ResponseEntity<RequestStatusDTO> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT);

        Assert.assertEquals("REJECTED", responseEntity.getBody().getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.INCIDENT, responseEntity.getBody().getType());
        Assert.assertEquals("Votre dérangement n'a pas abouti. Veuillez vous rapprocher du Service Client", responseEntity.getBody().getDescription());
        Assert.assertEquals("Rejetée", responseEntity.getBody().getTitle());
        Assert.assertEquals(false, responseEntity.getBody().getHistoric());

    }

    @Test
    public void getIncidentPendingStatus(){

        TroubleTicket troubleTicket = new TroubleTicket();
        troubleTicket.setTicketType(TroubleTicket.TicketTypeEnum.INCIDENT);
        troubleTicket.setStatus(TroubleTicket.StatusEnum.PENDING);

        Mockito.when(troubleTicketApiClient.getTroubleTicketById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT)).
            thenReturn(ResponseEntity.ok(troubleTicket));

        ResponseEntity<RequestStatusDTO> responseEntity = troubleTicketService.getRequestStatusById(REQUEST_ID,TroubleTicket.TicketTypeEnum.INCIDENT);

        Assert.assertEquals("PENDING", responseEntity.getBody().getStatus());
        Assert.assertEquals(REQUEST_ID, responseEntity.getBody().getRequestId());
        Assert.assertEquals(TroubleTicket.TicketTypeEnum.INCIDENT, responseEntity.getBody().getType());
        Assert.assertEquals("Nous n’avons réussi à vous joindre et/ou nous vous avons proposé un RDV pour vous joindre à nouveau", responseEntity.getBody().getDescription());
        Assert.assertEquals("ABSENTAVIS", responseEntity.getBody().getTitle());
        Assert.assertEquals(false, responseEntity.getBody().getHistoric());

    }
}
