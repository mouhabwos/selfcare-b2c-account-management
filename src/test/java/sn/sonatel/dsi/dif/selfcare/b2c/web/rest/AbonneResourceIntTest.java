package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AbonneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.TroubleTicketService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.PartyManagementApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.apimanagement.CustomerOfferService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RequestStatusDTO;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@IntegrationTest
public class AbonneResourceIntTest {

    private static final String DEFAULT_NUMERO = "771326617";
    private static final String DEFAULT_NUMERO_TO_VERIFIER = "771843646";
    private static final String DEFAULT_CODE = "123456";

    private MockMvc restAbonneMockMvc;

    @Mock
    private CustomerOfferService customerOfferService;

    @Mock
    private AbonneResource resource;

    @Mock
    private AbonneService abonneService;

    @Mock
    private TroubleTicketService troubleTicketService;

    @Autowired
    private AbonneService abonneServiceTest;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        final AbonneResource abonneResource = new AbonneResource( customerOfferService, abonneService, troubleTicketService);
        this.restAbonneMockMvc = MockMvcBuilders.standaloneSetup(abonneResource).build();

    }



    @Test
    public void isPostpaid() throws Exception {

        ResponseEntity<Boolean> response = ResponseEntity.status(HttpStatus.OK).build();
        when(resource.isPostpaid(Mockito.any(),Mockito.any())).thenReturn(response);

        restAbonneMockMvc.perform(get("/api/abonne/is-postpaid/{msisdn1}/{msisdn}", DEFAULT_NUMERO,DEFAULT_NUMERO_TO_VERIFIER))
                .andExpect(status().isOk());
    }

    @Test
    public void getRequestStatus() throws Exception {

        ResponseEntity<List<RequestStatusDTO>> response = ResponseEntity.status(HttpStatus.OK).build();
        when(resource.getRequestStatusById(Mockito.any())).thenReturn(response);

        restAbonneMockMvc.perform(get("/api/abonne/request/{requestId}", 51022830))
            .andExpect(status().isOk());
    }



    @Test
    public void getCustomerOffer() throws Exception {
        CustomerOffer customerOffer = new CustomerOffer();
        customerOffer.setClientCode("0012707812");
        customerOffer.setCreateDate("2011-05-26T15:51:10");
        customerOffer.setEndUserId("771326617");
        customerOffer.setOfferCode("9131");
        customerOffer.setOfferType(OfferTypeEnum.PREPAID);
        customerOffer.setOfferStatus("ACTIF");
        customerOffer.setOfferName("Jamono New Scool");

        ResponseEntity<CustomerOffer> response = ResponseEntity.status(HttpStatus.OK).body(customerOffer);
        when(customerOfferService.getCustomerOffer(Mockito.anyString())).thenReturn(customerOffer);

        restAbonneMockMvc.perform(get("/api/abonne/v1/customerOffer/{msisdn}", DEFAULT_NUMERO))
            .andExpect(status().isOk());
    }


    @Test
    public void testGetOrganizationInformationInformation() throws Exception {
         PartyManagementApiClient partyManagementApiClient = mock(PartyManagementApiClient.class);
         ResponseEntity response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        when(partyManagementApiClient.getOrganizationInformation(Mockito.anyString())).thenReturn(response);
        when(partyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(response);

        restAbonneMockMvc.perform(get("/api/abonne/v1/is-orange-number/{msisdn}", "DEFAULT_NUMERO"))
            .andExpect(status().isOk())
            .andExpect(content().string("false"));
    }


    @Test
    public void testGetClientInfo() throws Exception {
        PartyManagementApiClient partyManagementApiClient = mock(PartyManagementApiClient.class);
        IndividualInformation individualInformation = new IndividualInformation();
        individualInformation.setBirthDate("1996-05-08");

        when(partyManagementApiClient.getIndividualInformation(Mockito.anyString())).thenReturn(ResponseEntity.ok(individualInformation));
        restAbonneMockMvc.perform(get("/api/abonne/infos-client/{msisdn}", "DEFAULT_NUMERO"))
            .andExpect(status().isOk());

    }


    @Test
    public void getCustomerOfferWithoutClientCode() throws Exception {
        CustomerOffer customerOffer = new CustomerOffer();
        customerOffer.setClientCode("0012707812");
        customerOffer.setCreateDate("2011-05-26T15:51:10");
        customerOffer.setEndUserId("771326617");
        customerOffer.setOfferCode("9131");
        customerOffer.setOfferType(OfferTypeEnum.PREPAID);
        customerOffer.setOfferStatus("ACTIF");
        customerOffer.setOfferName("Jamono New Scool");


        when(customerOfferService.getCustomerOffer(Mockito.anyString())).thenReturn(customerOffer);

        restAbonneMockMvc.perform(get("/api/abonne/v2/customerOffer/{msisdn}", DEFAULT_NUMERO))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.clientCode").isEmpty())
            .andExpect(jsonPath("$.offerName").value(customerOffer.getOfferName()))
            .andExpect(jsonPath("$.offerId").value(customerOffer.getOfferId()));
    }

    @Test
    public void testIsOrganizationNumber() throws Exception {

        restAbonneMockMvc.perform(get("/api/abonne/v1/is-coorporate-number/{msisdn}", "DEFAULT_NUMERO"))
            .andExpect(status().isOk())
            .andExpect(content().string("false"));

    }

    @Test
    public void testGetMyContactNumbers() throws Exception {

        restAbonneMockMvc.perform(get("/api/abonne/v1/contact-numbers/{msisdn}", "782363572"))
            .andExpect(status().isOk());

    }

    @Test
    public void testGetNumberStatus() throws Exception {

        when(abonneService.getNumberStatus(anyString())).thenReturn(ResponseEntity.ok("ACTIVATED"));
        restAbonneMockMvc.perform(get("/api/abonne/v1/number/{msisdn}/status", "338328033"))
            .andExpect(status().isOk())
            .andExpect(content().string("ACTIVATED"));
    }


    @Test
    public void testGetNumberStatusWithError() throws Exception {

        final AbonneResource abonneResource = new AbonneResource( customerOfferService, abonneServiceTest, troubleTicketService);
        this.restAbonneMockMvc = MockMvcBuilders.standaloneSetup(abonneResource).build();

       /* restAbonneMockMvc.perform(get("/api/abonne/v1/number/{msisdn}/status", "330000000"))
            .andExpect(status().isNotFound());*/
    }



}
