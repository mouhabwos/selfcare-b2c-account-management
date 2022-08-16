package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.createFormattingConversionService;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.service.LoginAttemptService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;

@RunWith(SpringRunner.class)
@IntegrationTest
class AuthResourceTest {

    private static final String DEFAULT_USERNAME = "781326060";

    @Autowired
    private LoginAttemptService loginAttemptService;

    private MockMvc restAccountB2CMockMvc;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private EntityManager em;

    private AccountB2C accountB2C;

    static AccountB2C createEntity(EntityManager em) {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero(DEFAULT_USERNAME);
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test77@gmail.com");
        accountB2C.setImageProfil("image");

        return accountB2C;
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        final AuthResource authResource = new AuthResource(loginAttemptService);

        this.restAccountB2CMockMvc =
            MockMvcBuilders
                .standaloneSetup(authResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator)
                .build();
    }

    @BeforeEach
    void initTest() {
        accountB2C = createEntity(em);
    }

    @Test
    void loginSucceeded() throws Exception {
        restAccountB2CMockMvc.perform(get("/api/auth/login-succeeded/{username}", accountB2C.getNumero())).andExpect(status().isOk());
    }

    @Test
    void loginFailed() throws Exception {
        restAccountB2CMockMvc.perform(get("/api/auth/login-failed/{username}", accountB2C.getNumero())).andExpect(status().isOk());
    }
}
