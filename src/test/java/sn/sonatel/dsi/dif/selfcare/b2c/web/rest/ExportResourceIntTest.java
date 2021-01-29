package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.export.ExportService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.ExportResource;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.SponsorResource;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.createFormattingConversionService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SelfcareB2CApp.class})
public class ExportResourceIntTest {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    @Autowired
    private ExportService exportService;

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    private MockMvc restMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExportResource exportResource = new ExportResource(exportService);
        this.restMockMvc = MockMvcBuilders.standaloneSetup(exportResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @Test
    @Transactional
    public void testExportAllUsers() throws Exception {

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("export@orange-sonatel.com");

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("770000000");
        accountB2C.setFirstName("");
        accountB2C.setLastName("");

        accountB2CRepository.save(accountB2C);

        AccountB2C account2 = new AccountB2C();
        account2.setNumero("770000001");
        account2.setFirstName("");
        account2.setLastName("");

        accountB2CRepository.save(account2);

        restMockMvc.perform(post("/api/export/all-users").principal(mockPrincipal))
            .andExpect(status().isAccepted());

    }
}
