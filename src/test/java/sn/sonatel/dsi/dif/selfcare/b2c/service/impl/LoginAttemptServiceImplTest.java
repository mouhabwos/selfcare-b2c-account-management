package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.AccountStatus;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.NotificationInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SMSNotificationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {LoginAttemptServiceImpl.class, ApplicationProperties.class,
    SMSNotificationService.class})
@ExtendWith(SpringExtension.class)
@IntegrationTest
class LoginAttemptServiceImplTest {

    @MockBean
    private AccountB2CRepository accountB2CRepository;

    @Autowired
    private LoginAttemptServiceImpl loginAttemptServiceImpl;

    private static final String DEFAULT_USERNAME = "781320607";
    private static final String DEFAULT_USERNAME2 = "781300101";

    private LoginAttemptServiceImpl loginAttemptService;

    @Spy
    private AccountB2CRepository b2CRepository;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private SMSNotificationService smsNotificationService;

    @Mock
    private NotificationInformationService notificationInformationService;

    private void createEntity4() {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero(DEFAULT_USERNAME);
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test07@gmail.com");
        accountB2C.setImageProfil("image");
        b2CRepository.save(accountB2C);
    }

    private void createEntity2() {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero(DEFAULT_USERNAME2);
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test70@gmail.com");
        accountB2C.setImageProfil("image");
        accountB2C.setDerniereConnnexionDate(ZonedDateTime.now());
        accountB2C.setAttempts(0);
        b2CRepository.save(accountB2C);
    }

    private void createEntity3() {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("789009090");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test90@gmail.com");
        accountB2C.setImageProfil("image");
        accountB2C.setDerniereConnnexionDate(ZonedDateTime.now());
        accountB2C.setAttempts(0);
        b2CRepository.save(accountB2C);
    }

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        loginAttemptService =
            new LoginAttemptServiceImpl(b2CRepository, applicationProperties, smsNotificationService, notificationInformationService);
    }

    @Test
    void loginSucceeded() {
        createEntity2();
        loginAttemptService.loginSucceeded(DEFAULT_USERNAME2);
        Mockito.verify(b2CRepository).save(Mockito.any(AccountB2C.class));
    }

    @Test
    void loginFailedIsBlocked() {
        createEntity4();
        for (int i = 0; i <= 3; i++) {
            loginAttemptService.loginFailed(DEFAULT_USERNAME);
            Mockito.verify(b2CRepository).save(Mockito.any(AccountB2C.class));
        }
    }

    /**
     * Method under test: {@link LoginAttemptServiceImpl#loginFailed(String)}
     */
    @Test
    void testLoginFailedWithDerniereConnnexionDateNotInIntervall() {

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.email("jane.doe@example.org");
        accountB2C.firstName("Jane");
        accountB2C.imageProfil("Image Profil");
        accountB2C.lastName("Doe");
        accountB2C.numero("Numero");
        accountB2C.setAccountStatus(AccountStatus.FULL);
        accountB2C.setActivationKey("Activation Key");
        accountB2C.setAttempts(2);
        accountB2C.setClientId("42");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountB2C.setCreatedDate(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());
        accountB2C.setDerniereConnnexionDate(ZonedDateTime.now().minusDays(10));
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
        Optional<AccountB2C> ofResult = Optional.of(accountB2C);

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
        when(accountB2CRepository.save((AccountB2C) any())).thenReturn(accountB2C1);
        when(accountB2CRepository.findOneByNumero((String) any())).thenReturn(ofResult);
        var attemps = loginAttemptServiceImpl.loginFailed("janedoe");
        Assertions.assertTrue(attemps==2);
    }

    /**
     * Method under test: {@link LoginAttemptServiceImpl#loginFailed(String)}
     */
    @Test
    void testLoginFailedWithDerniereConnnexionDate() {

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.email("jane.doe@example.org");
        accountB2C.firstName("Jane");
        accountB2C.imageProfil("Image Profil");
        accountB2C.lastName("Doe");
        accountB2C.numero("Numero");
        accountB2C.setAccountStatus(AccountStatus.FULL);
        accountB2C.setActivationKey("Activation Key");
        accountB2C.setAttempts(2);
        accountB2C.setClientId("42");
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountB2C.setCreatedDate(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant());
        accountB2C.setDerniereConnnexionDate(ZonedDateTime.now());
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
        Optional<AccountB2C> ofResult = Optional.of(accountB2C);

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
        when(accountB2CRepository.save((AccountB2C) any())).thenReturn(accountB2C1);
        when(accountB2CRepository.findOneByNumero((String) any())).thenReturn(ofResult);
        var attemps = loginAttemptServiceImpl.loginFailed("janedoe");
        Assertions.assertTrue(attemps==0);
    }

    /**
     * Method under test: {@link LoginAttemptServiceImpl#loginFailed(String)}
     */
    @Test
    void testLoginFailedWithEmptyDerniereConnnexionDate() {

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
        Optional<AccountB2C> ofResult = Optional.of(accountB2C);

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
        when(accountB2CRepository.save((AccountB2C) any())).thenReturn(accountB2C1);
        when(accountB2CRepository.findOneByNumero((String) any())).thenReturn(ofResult);
        var attemps = loginAttemptServiceImpl.loginFailed("janedoe");
        Assertions.assertTrue(attemps>0);
    }


    /**
     * Method under test: {@link LoginAttemptServiceImpl#loginFailed(String)}
     */
    @Test
    void testLoginFailed3() {
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
        when(accountB2CRepository.save((AccountB2C) any())).thenReturn(accountB2C);
        when(accountB2CRepository.findOneByNumero((String) any())).thenReturn(Optional.empty());
        assertEquals(-1, loginAttemptServiceImpl.loginFailed("janedoe"));
        verify(accountB2CRepository).findOneByNumero((String) any());
    }


    @Test
    void isBlocked() {
        createEntity3();
        boolean check = loginAttemptService.isBlocked("789009090");

        assertThat(check).isFalse();
    }
}
