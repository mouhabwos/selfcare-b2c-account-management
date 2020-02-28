package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.NotificationInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.NotificationInformationRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.NotificationInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NotificationInformationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.createFormattingConversionService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelfcareB2CApp.class})
public class NotificationInformationResourceTest {

    @Autowired
    private NotificationInformationService mockNotificationInformationService;

    private NotificationInformationResource notificationInformationResourceUnderTest;

    private MockMvc restMockMvc;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    @Autowired
    private NotificationInformationRepository notificationInformationRepository;

    @Before
    public void setUp() {

        notificationInformationResourceUnderTest = new NotificationInformationResource(mockNotificationInformationService);
        this.restMockMvc = MockMvcBuilders.standaloneSetup(notificationInformationResourceUnderTest)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }

    @Test
    public void testGetNotificationInformationByCodeFormule() throws Exception {

        // Setup
        restMockMvc.perform(get("/api/notification-information/{msisdn}", "40401"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("[]"));

    }

    @Test
    public void testUpdateCodeFormuleByMsisdnNoExistingAccount() throws Exception {

        NotificationInformationDTO informationDTO = new NotificationInformationDTO();
        informationDTO.setCodeFormule("40401");
        informationDTO.setMsisdn("msisdn");

        // Setup
        restMockMvc.perform(put("/api/notification-information")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(informationDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateCodeFormuleByMsisdn() throws Exception {

        String codeUpdate = "7777";

        //Save account

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("hello");
        accountB2C.setFirstName("hello");
        accountB2C.setNumero("770010101");
        accountB2C.setEmail("test785066@gmail.com");
        accountB2C = accountB2CRepository.save(accountB2C);

        NotificationInformation information = new NotificationInformation();
        information.setCodeFormule("7878");
        information.setFirebaseId("000001");
        information.setAccountB2C(accountB2C);

        notificationInformationRepository.save(information);

        NotificationInformationDTO informationDTO = new NotificationInformationDTO();
        informationDTO.setCodeFormule(codeUpdate);
        informationDTO.setFirebaseId("888888");
        informationDTO.setMsisdn(accountB2C.getNumero());

        // Setup
        restMockMvc.perform(put("/api/notification-information")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(informationDTO)))
            .andExpect(status().isOk());

    }
}
