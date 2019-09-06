package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareSoapService;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SelfcareB2CApp.class})
public class AbonneResourceIntTest {

    private static final String DEFAULT_NUMERO = "771326617";
    private static final String DEFAULT_NUMERO_TO_VERIFIER = "771843646";
    private static final String DEFAULT_CODE = "123456";

    private MockMvc restAbonneMockMvc;

    @Mock
    private SelfcareSoapService soapService;

    @Mock
    private CustomerOfferApiClient customerOfferApiClient;

    @Mock
    private AbonneResource resource;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        final AbonneResource abonneResource = new AbonneResource(soapService, customerOfferApiClient);
        this.restAbonneMockMvc = MockMvcBuilders.standaloneSetup(abonneResource).build();

    }

    @Test
    public void getSouscription() throws Exception {

        ResponseEntity<SouscriptionDto> response = ResponseEntity.status(HttpStatus.OK).build();
        when(resource.getSouscription(Mockito.any())).thenReturn(response);

       restAbonneMockMvc.perform(get("/api/abonne/souscription/{msisdn}", DEFAULT_NUMERO))
            .andExpect(status().isOk());
    }

    @Test
    public void isPostpaid() throws Exception {

        ResponseEntity<Boolean> response = ResponseEntity.status(HttpStatus.OK).build();
        when(resource.isPostpaid(Mockito.any(),Mockito.any())).thenReturn(response);

        restAbonneMockMvc.perform(get("/api/abonne/is-postpaid/{msisdn1}/{msisdn}", DEFAULT_NUMERO,DEFAULT_NUMERO_TO_VERIFIER))
                .andExpect(status().isOk());
    }

    @Test
    public void getAbonne() throws Exception {
        ResponseEntity<AbonneDTO> response = ResponseEntity.status(HttpStatus.OK).build();
        when(resource.getAbonne(Mockito.anyString(),Mockito.anyString())).thenReturn(response);

        restAbonneMockMvc.perform(get("/api/abonne/information-abonne/{msisdn}/{code}", DEFAULT_NUMERO,DEFAULT_CODE))
            .andExpect(status().isOk());
    }

    @Test
    public void getAbonneWithStatusNOT_FOUND() throws Exception {

        restAbonneMockMvc.perform(get("/api/abonne/information-abonne/{msisdn}", ""))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getSouscriptionWithStatusNOT_FOUND() throws Exception {

        restAbonneMockMvc.perform(get("/api/abonne/souscription/{msisdn}", ""))
            .andExpect(status().isNotFound());
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
        when(customerOfferApiClient.getCustomerOffer(Mockito.anyString())).thenReturn(response);

        restAbonneMockMvc.perform(get("/api/abonne/v1/customerOffer/{msisdn}", DEFAULT_NUMERO))
            .andExpect(status().isOk());
    }

    @Test
    public void getCustomerOfferWithNullBody() throws Exception {

        ResponseEntity<CustomerOffer> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        when(customerOfferApiClient.getCustomerOffer(Mockito.anyString())).thenReturn(response);

        restAbonneMockMvc.perform(get("/api/abonne/v1/customerOffer/{msisdn}", "DEFAULT_NUMERO"))
            .andExpect(status().isNotFound());
    }


}
