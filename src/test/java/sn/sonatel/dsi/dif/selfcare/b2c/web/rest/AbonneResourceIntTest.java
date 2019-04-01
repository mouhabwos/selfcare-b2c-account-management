package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SelfcareB2CApp.class})
public class AbonneResourceIntTest {

    private static final String DEFAULT_NUMERO = "771326617";

    private MockMvc restAbonneMockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;


    @Autowired
    private Validator validator;

    @Autowired
    private SelfcareSoapService selfcareSoapService;

    @Autowired
    private AbonneResource abonneResource;

    private ClientAndServer mockServer;

    @Autowired
    private AbonneResource abonneResources;

    @Mock
    private SelfcareSoapService soapService;

    @Mock
    private AbonneResource resource;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        final AbonneResource abonneResource = new AbonneResource(soapService);
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
    public void getAbonne() throws Exception {
        ResponseEntity<List<AbonneDTO>> response = ResponseEntity.status(HttpStatus.OK).build();
        when(resource.getAbonne(Mockito.any())).thenReturn(response);

        restAbonneMockMvc.perform(get("/api/abonne/information-abonne/{msisdn}", "771326617"))
            .andExpect(status().isOk());
    }

    @Test
    public void getAbonneServiceUnavailable() throws Exception {

        ResponseEntity<List<AbonneDTO>> response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest(DEFAULT_NUMERO));
        //Mockito.any()
        when(soapService.getAbonne(request)).thenReturn(response);

    }

    @Test
    public void getSouscriptionServiceUnavailable() throws Exception {

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest(DEFAULT_NUMERO));
        ResponseEntity<SouscriptionDto> response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        when(soapService.getSouscription(request)).thenReturn(response);

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



}
