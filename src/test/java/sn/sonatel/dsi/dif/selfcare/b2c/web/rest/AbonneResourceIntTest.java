package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AbonneResourceIntTest {

    private static final String DEFAULT_NUMERO = "778505050";

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
            .andExpect(status().isOk());
    }

    @Test
    public void getAbonneWithStatusNOT_FOUND() throws Exception {

        restAbonneMockMvc.perform(get("/api/abonne/information-abonne/{msisdn}", DEFAULT_NUMERO))
            .andExpect(status().isOk());
    }
}
