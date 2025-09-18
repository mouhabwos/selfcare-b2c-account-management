package sn.sonatel.dsi.dif.selfcare.b2c.service;

import static org.mockito.MockitoAnnotations.initMocks;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;

@RunWith(SpringRunner.class)
@IntegrationTest
class SchedulerServiceTest {

    @Autowired
    private SponseeRepository mockSponseeRepository;

    private SchedulerService schedulerServiceUnderTest;

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    @Mock
    private AbonneService abonneService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp() {
        initMocks(this);
        schedulerServiceUnderTest = new SchedulerService(mockSponseeRepository, accountB2CRepository, abonneService, applicationProperties);
    }

    private AccountB2C getAccount() {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setId(785L);
        accountB2C.setNumero("770000055");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");

        return accountB2C;
    }

    private Sponsee getSponsee() {
        Sponsee sponsee = new Sponsee();
        sponsee.setMsisdn("770000006");
        sponsee.setAccountB2C(getAccount());

        return sponsee;
    }

    @Test
    void testDisabledSponsee() {
        AccountB2C account = getAccount();
        AccountB2C accountB2C = accountB2CRepository.save(account);
        Sponsee sponsee = getSponsee();
        sponsee.setMsisdn("778888888");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse("2019-09-29T10:00:00+00:00[Africa/Dakar]");

        sponsee.setCreatedDate(zonedDateTime);
        sponsee.setAccountB2C(accountB2C);
        sponsee.setEnabled(true);
        mockSponseeRepository.save(sponsee);

        schedulerServiceUnderTest.disabledSponsee();

        Optional<Sponsee> sponseeOptional = mockSponseeRepository.findOneByMsisdn(sponsee.getMsisdn());

        Assert.assertFalse(!sponsee.isEnabled());
        Assert.assertEquals(sponsee.getAccountB2C().getNumero(), sponseeOptional.get().getAccountB2C().getNumero());
        Assert.assertEquals(sponsee.getFirstName(), sponseeOptional.get().getFirstName());
        Assert.assertEquals(sponsee.getLastName(), sponseeOptional.get().getLastName());
    }

    @Test
    void testUpdateFirstnameAndLastname() {
        accountB2CRepository.deleteAll();

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

        AbonneDTO abonneDTO = new AbonneDTO();
        abonneDTO.setPrenomAbonne("prenom");
        abonneDTO.setNomAbonne("nom");

        Mockito.when(abonneService.getInformationAbonne(Mockito.anyString())).thenReturn(abonneDTO);

        schedulerServiceUnderTest.updateFirstnameAndLastname();

        List<AccountB2C> b2CList = accountB2CRepository.findAll();

        Assert.assertEquals(2, b2CList.size());
        Assert.assertEquals(abonneDTO.getNomAbonne(), b2CList.get(0).getLastName());
        Assert.assertEquals(abonneDTO.getPrenomAbonne(), b2CList.get(0).getFirstName());
    }
}
