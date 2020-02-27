package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.service.NotificationInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NotificationInformationDTO;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelfcareB2CApp.class})
public class NotificationInformationResourceTest {

    @Autowired
    private NotificationInformationService mockNotificationInformationService;

    private NotificationInformationResource notificationInformationResourceUnderTest;

    private MockMvc restMockMvc;

    @Before
    public void setUp() {

        notificationInformationResourceUnderTest = new NotificationInformationResource(mockNotificationInformationService);
        this.restMockMvc = MockMvcBuilders.standaloneSetup(notificationInformationResourceUnderTest).build();
    }

    @Test
    public void testGetNotificationInformationByCodeFormule() throws Exception {

        // Setup
        restMockMvc.perform(get("/api/notification-information/{msisdn}", "40401"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("[]"));

    }
}
