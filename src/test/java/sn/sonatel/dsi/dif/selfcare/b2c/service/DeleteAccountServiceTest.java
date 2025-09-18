package sn.sonatel.dsi.dif.selfcare.b2c.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.AccountStatus;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;

import static org.mockito.Mockito.*;

@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class,classes = {DeleteAccountService.class, SMSNotificationService.class, ApplicationProperties.class})
@EnableConfigurationProperties(value = ApplicationProperties.class)
@TestPropertySource("file:src/test/resources/config/application.yml")
@ExtendWith(SpringExtension.class)
class DeleteAccountServiceTest {
    @MockBean
    private AccountB2CRepository accountB2CRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private DeleteAccountService deleteAccountService;

    @MockBean
    private RattachementLigneRepository rattachementLigneRepository;

    /**
     * Method under test: {@link DeleteAccountService#purgeNumberInfos(String)}
     */
    @Test
    void testPurgeNumberInfos() {

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
        doNothing().when(accountB2CRepository).deleteById((Long) any());
        when(accountB2CRepository.findOneByNumero((String) any())).thenReturn(ofResult);

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

        AccountB2C accountB2C2 = new AccountB2C();
        accountB2C2.email("jane.doe@example.org");
        accountB2C2.firstName("Jane");
        accountB2C2.imageProfil("Image Profil");
        accountB2C2.lastName("Doe");
        accountB2C2.numero("Numero");
        accountB2C2.setAccountStatus(AccountStatus.FULL);
        accountB2C2.setActivationKey("Activation Key");
        accountB2C2.setAttempts(1);
        accountB2C2.setClientId("42");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountB2C2.setCreatedDate(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant());
        accountB2C2.setDerniereConnnexionDate(null);
        accountB2C2.setDeviceInfos(new HashSet<>());
        accountB2C2.setEmail("jane.doe@example.org");
        accountB2C2.setEmailActivated(true);
        accountB2C2.setFirstName("Jane");
        accountB2C2.setHashMsisdn("Hash Msisdn");
        accountB2C2.setId(123L);
        accountB2C2.setImageProfil("Image Profil");
        accountB2C2.setLangKey("Lang Key");
        accountB2C2.setLastName("Doe");
        accountB2C2.setNotificationInformations(new HashSet<>());
        accountB2C2.setNumero("Numero");
        accountB2C2.setSponsees(new HashSet<>());
        accountB2C2.setUsers(new HashSet<>());
        accountB2C2.users(new HashSet<>());

        RattachementLigne rattachementLigne = new RattachementLigne();
        rattachementLigne.accountB2C(accountB2C1);
        rattachementLigne.numero("Numero");
        rattachementLigne.setAccountB2C(accountB2C2);
        rattachementLigne.setId(123L);
        rattachementLigne.setIdClient("Id Client");
        rattachementLigne.setNumero("Numero");
        rattachementLigne.setTypeNumero(TypeNumero.FIXE);
        rattachementLigne.typeNumero(TypeNumero.FIXE);
        Optional<RattachementLigne> ofResult1 = Optional.of(rattachementLigne);
        doNothing().when(rattachementLigneRepository).deleteById((Long) any());
        when(rattachementLigneRepository.findAllByAccountB2C((AccountB2C) any())).thenReturn(Arrays.asList(rattachementLigne));
        when(rattachementLigneRepository.findByNumero((String) any())).thenReturn(ofResult1);
        deleteAccountService.purgeNumberInfos("Msisdn");
        verify(accountB2CRepository, atLeast(1)).findOneByNumero((String) any());
        verify(accountB2CRepository,atLeast(1)).deleteById((Long) any());
        verify(rattachementLigneRepository,atLeast(1)).findAllByAccountB2C((AccountB2C) any());
        verify(rattachementLigneRepository,atLeast(1)).findByNumero((String) any());
    }

    /**
     * Method under test: {@link DeleteAccountService#purgeNumberInfos(String)}
     */
    @Test
    void testPurgeNumberInfos2() {
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
        doNothing().when(accountB2CRepository).deleteById((Long) any());
        when(accountB2CRepository.findOneByNumero((String) any())).thenReturn(ofResult);
        doNothing().when(rattachementLigneRepository).deleteById((Long) any());
        when(rattachementLigneRepository.findAllByAccountB2C((AccountB2C) any())).thenReturn(new ArrayList<>());
        when(rattachementLigneRepository.findByNumero((String) any())).thenReturn(Optional.empty());
        deleteAccountService.purgeNumberInfos("Msisdn");
        verify(accountB2CRepository).findOneByNumero((String) any());
        verify(accountB2CRepository).deleteById((Long) any());
        verify(rattachementLigneRepository).findAllByAccountB2C((AccountB2C) any());
        verify(rattachementLigneRepository).findByNumero((String) any());
    }
}

