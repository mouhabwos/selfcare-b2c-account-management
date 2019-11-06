package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelfcareB2CApp.class})
public class SchedulerServiceTest {

    @Autowired
    private SponseeRepository mockSponseeRepository;

    private SchedulerService schedulerServiceUnderTest;

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    @Before
    public void setUp() {
        initMocks(this);
        schedulerServiceUnderTest = new SchedulerService(mockSponseeRepository);
    }

    private AccountB2C getAccount(){
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setId(785L);
        accountB2C.setNumero("770000055");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");

        return accountB2C;
    }

    private Sponsee getSponsee(){

        Sponsee sponsee = new Sponsee();
        sponsee.setMsisdn("770000006");
        sponsee.setAccountB2C(getAccount());

        return sponsee;
    }


    @Test
    public void testDisabledSponsee() {
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
        Assert.assertEquals(sponsee.getAccountB2C().getNumero(),sponseeOptional.get().getAccountB2C().getNumero());
        Assert.assertEquals(sponsee.getFirstName(),sponseeOptional.get().getFirstName());
        Assert.assertEquals(sponsee.getLastName(),sponseeOptional.get().getLastName());
    }
}
