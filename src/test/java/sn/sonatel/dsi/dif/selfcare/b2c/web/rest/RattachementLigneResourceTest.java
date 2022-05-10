package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;

import org.hibernate.engine.spi.SessionDelegatorBaseImpl;
import org.hibernate.engine.spi.SessionImplementor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.AccountStatus;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.service.RattachementLigneService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLigneCNIVM;

@ContextConfiguration(classes = {RattachementLigneResource.class})
@ExtendWith(SpringExtension.class)
class RattachementLigneResourceTest {
    @Autowired
    private RattachementLigneResource rattachementLigneResource;

    @MockBean
    private RattachementLigneService rattachementLigneService;

    /**
     * Method under test: {@link RattachementLigneResource#rattachementLigneByOtp(RattachementLigneCNIVM)}
     */
    @Test
    void testRattachementLigneByOtpShouldReturnBadRequest() throws Exception {
        RattachementLigneCNIVM rattachementLigneCNIVM = new RattachementLigneCNIVM();
        rattachementLigneCNIVM.numero("Numero");
        rattachementLigneCNIVM.setIdentificationId("42");
        rattachementLigneCNIVM.setLogin("Login");
        rattachementLigneCNIVM.setNumero("Numero");
        rattachementLigneCNIVM.setTypeNumero(TypeNumero.FIXE);
        rattachementLigneCNIVM.typeNumero(TypeNumero.FIXE);
        String content = (new ObjectMapper()).writeValueAsString(rattachementLigneCNIVM);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/rattachement-lignes/register/by-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.rattachementLigneResource)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link RattachementLigneResource#rattachementLigneByOtp(RattachementLigneCNIVM)}
     */
    @Test
    void testRattachementLigneByOtpShouldReturnCreated() throws Exception {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.email("jane.doe@example.org");
        accountB2C.firstName("Jane");
        accountB2C.imageProfil("Image Profil");
        accountB2C.lastName("Doe");
        accountB2C.numero("Numero");
        accountB2C.setAccountStatus(AccountStatus.FULL);
        accountB2C.setActivationKey("Activation Key");
        accountB2C.setAttempts(1);
        accountB2C.setClientId("42");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountB2C.setCreatedDate(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());
        accountB2C.setDerniereConnnexionDate(null);
        accountB2C.setDeviceInfos(new HashSet<>());
        accountB2C.setEmail("jane.doe@example.org");
        accountB2C.setEmailActivated(true);
        accountB2C.setFirstName("Jane");
        accountB2C.setHashMsisdn("Hash Msisdn");
        accountB2C.setId(123L);
        accountB2C.setImageProfil("Image Profil");
        accountB2C.setLangKey("Lang Key");
        accountB2C.setLastName("Doe");
        accountB2C.setNotificationInformations(new HashSet<>());
        accountB2C.setNumero("Numero");
        accountB2C.setSponsees(new HashSet<>());
        accountB2C.setUsers(new HashSet<>());
        accountB2C.users(new HashSet<>());

        AccountB2C accountB2C1 = new AccountB2C();
        accountB2C1.email("jane.doe@example.org");
        accountB2C1.firstName("Jane");
        accountB2C1.imageProfil("Image Profil");
        accountB2C1.lastName("Doe");
        accountB2C1.numero("Numero");
        accountB2C1.setAccountStatus(AccountStatus.FULL);
        accountB2C1.setActivationKey("Activation Key");
        accountB2C1.setAttempts(1);
        accountB2C1.setClientId("42");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountB2C1.setCreatedDate(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant());
        accountB2C1.setDerniereConnnexionDate(null);
        accountB2C1.setDeviceInfos(new HashSet<>());
        accountB2C1.setEmail("jane.doe@example.org");
        accountB2C1.setEmailActivated(true);
        accountB2C1.setFirstName("Jane");
        accountB2C1.setHashMsisdn("Hash Msisdn");
        accountB2C1.setId(123L);
        accountB2C1.setImageProfil("Image Profil");
        accountB2C1.setLangKey("Lang Key");
        accountB2C1.setLastName("Doe");
        accountB2C1.setNotificationInformations(new HashSet<>());
        accountB2C1.setNumero("Numero");
        accountB2C1.setSponsees(new HashSet<>());
        accountB2C1.setUsers(new HashSet<>());
        accountB2C1.users(new HashSet<>());

        RattachementLigne rattachementLigne = new RattachementLigne();
        rattachementLigne.accountB2C(accountB2C);
        rattachementLigne.numero("Numero");
        rattachementLigne.setAccountB2C(accountB2C1);
        rattachementLigne.setId(123L);
        rattachementLigne.setIdClient("Id Client");
        rattachementLigne.setNumero("Numero");
        rattachementLigne.setTypeNumero(TypeNumero.FIXE);
        rattachementLigne.typeNumero(TypeNumero.FIXE);
        when(this.rattachementLigneService.rattachementLigneByOtp((RattachementLigneCNIVM) any()))
                .thenReturn(rattachementLigne);

        RattachementLigneCNIVM rattachementLigneCNIVM = new RattachementLigneCNIVM();
        rattachementLigneCNIVM.numero("Numero");
        rattachementLigneCNIVM.setIdentificationId("42");
        rattachementLigneCNIVM.setLogin("Login");
        rattachementLigneCNIVM.setNumero("00221779999999");
        rattachementLigneCNIVM.setTypeNumero(TypeNumero.FIXE);
        rattachementLigneCNIVM.typeNumero(TypeNumero.FIXE);
        String content = (new ObjectMapper()).writeValueAsString(rattachementLigneCNIVM);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/rattachement-lignes/register/by-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.rattachementLigneResource)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"numero\":\"Numero\",\"typeNumero\":\"FIXE\",\"accountB2C\":{\"id\":123,\"numero\":\"Numero\",\"firstName\""
                                        + ":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"imageProfil\":\"Image Profil\",\"clientId\":\"42\""
                                        + ",\"hashMsisdn\":\"Hash Msisdn\",\"activationKey\":\"Activation Key\",\"langKey\":\"Lang Key\",\"attempts\":1,"
                                        + "\"createdDate\":0.0,\"sponsees\":[],\"deviceInfos\":[],\"derniereConnnexionDate\":null,\"tutoViewed\":false,"
                                        + "\"emailActivated\":true,\"notificationInformations\":[],\"accountStatus\":\"FULL\"},\"idClient\":\"Id Client\"}"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/api/rattachement-lignes/123"));
    }

}

