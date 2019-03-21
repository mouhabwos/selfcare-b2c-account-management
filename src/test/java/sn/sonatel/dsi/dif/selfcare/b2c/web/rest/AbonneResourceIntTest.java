package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
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



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        final AbonneResource abonneResource = new AbonneResource(selfcareSoapService);
        this.restAbonneMockMvc = MockMvcBuilders.standaloneSetup(abonneResource).build();
    }

    @Test
    public void getSouscription() throws Exception {

        restAbonneMockMvc.perform(get("/api/abonne/souscription/{msisdn}", DEFAULT_NUMERO))
            .andExpect(status().isOk());
    }

    @Test
    public void getAbonne() throws Exception {

        restAbonneMockMvc.perform(get("/api/abonne/information-abonne/{msisdn}", DEFAULT_NUMERO))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getAbonneWithStatusNOT_FOUND() throws Exception {

        restAbonneMockMvc.perform(get("/api/abonne/information-abonne/{msisdn}", DEFAULT_NUMERO))
            .andExpect(status().isNotFound());
    }

    @Test
    public void mockGetAbonne() throws Exception {

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest(DEFAULT_NUMERO));
        // Mocking service
       // when(selfcareSoapService.getAbonne(request)).thenReturn();
        MvcResult result = restAbonneMockMvc.perform(get("/api/abonne/information-abonne/", DEFAULT_NUMERO).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            //.andDo(print())
            // .andExpect(status().is2xxSuccessful()).andReturn();
            .andReturn();
//        restAbonneMockMvc.perform(asyncDispatch(result))
           // .andDo(print())
          //  .andExpect(status().isOk());
            //.andExpect(jsonPath("$[0].title", is("Hokuto no ken")));
    }
}
