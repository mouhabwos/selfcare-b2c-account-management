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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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



    @Test
    public void testGetFirebaseIdByMsisdn() throws Exception {

        List<String> stringList = new ArrayList<>();
        stringList.add("770010101");
        stringList.add("770000001");
        stringList.add("770000002");
        stringList.add("770000003");

        // Setup
        restMockMvc.perform(get("/api/notification-information")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stringList)))
            .andExpect(status().isOk());
    }

    @Test
    public void testRegisterInformationPresent() throws Exception {

        String codeUpdate = "7777";

        //Save account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("hello");
        accountB2C.setFirstName("hello");
        accountB2C.setNumero("770010111");
        accountB2C.setEmail("test785016@gmail.com");
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
        restMockMvc.perform(post("/api/notification-information/register")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(informationDTO)))
            .andExpect(status().isBadRequest());

    }



    @Test
    public void testRegisterNoPresent() throws Exception {



        NotificationInformationDTO informationDTO = new NotificationInformationDTO();
        informationDTO.setCodeFormule("9696");
        informationDTO.setFirebaseId("888888");
        informationDTO.setMsisdn("770003636");

        // Setup
        restMockMvc.perform(post("/api/notification-information/register")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(informationDTO)))
            .andExpect(status().isBadRequest());

    }

    @Test
    public void testRegisterCreated() throws Exception {

        //Save account

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("hello");
        accountB2C.setFirstName("hello");
        accountB2C.setNumero("770010115");
        accountB2C.setEmail("test785015@gmail.com");
        accountB2C = accountB2CRepository.save(accountB2C);


        NotificationInformationDTO informationDTO = new NotificationInformationDTO();
        informationDTO.setCodeFormule("0012");
        informationDTO.setFirebaseId("888896");
        informationDTO.setMsisdn(accountB2C.getNumero());

        // Setup
        restMockMvc.perform(post("/api/notification-information/register")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(informationDTO)))
            .andExpect(status().isCreated());

    }


    @Test
    public void testGetFirebaseIdByListCodeFormule() throws Exception {

        //Save account

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("hello");
        accountB2C.setFirstName("hello");
        accountB2C.setNumero("780010115");
        accountB2C.setEmail("test775015@gmail.com");
        accountB2C = accountB2CRepository.save(accountB2C);

        //    save notification information
        NotificationInformation information = new NotificationInformation();
        information.setAccountB2C(accountB2C);
        information.setCodeFormule("9131");
        notificationInformationRepository.save(information);

        List<String> stringList = new ArrayList<>();
        stringList.add("9131");
        stringList.add("8080");

        // Setup
        restMockMvc.perform(get("/api/notification-information/by-codesFormule?codeFormule=9131,8080")
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].msisdn").value(hasItem(accountB2C.getNumero())))
            .andExpect(jsonPath("$.[*].codeFormule").value(hasItem(information.getCodeFormule())));
    }


    @Test
    public void testUpdateCodeFormuleByMsisdnNoExistingInfo() throws Exception {

        String codeUpdate = "7779";

        //Save account

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("hello");
        accountB2C.setFirstName("hello");
        accountB2C.setNumero("770010152");
        accountB2C.setEmail("test785066@gmail.com");
        accountB2C = accountB2CRepository.save(accountB2C);

        NotificationInformationDTO informationDTO = new NotificationInformationDTO();
        informationDTO.setCodeFormule(codeUpdate);
        informationDTO.setFirebaseId("888");
        informationDTO.setMsisdn(accountB2C.getNumero());

        // Setup
        restMockMvc.perform(put("/api/notification-information")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(informationDTO)))
            .andExpect(status().isOk());

    }
}
