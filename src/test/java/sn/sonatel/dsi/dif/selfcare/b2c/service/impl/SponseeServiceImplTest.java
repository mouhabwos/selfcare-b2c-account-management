package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.SponseeMapper;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ForbiddenException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelfcareB2CApp.class})
public class SponseeServiceImplTest {

    @Mock
    private SponseeRepository mockSponseeRepository;
    @Mock
    private SponseeMapper mockSponseeMapper;
    @Autowired
    private ApplicationProperties mockApplicationProperties;
    @Mock
    private ServicesOTP mockServicesOTP;

    private SponseeServiceImpl sponseeServiceImplUnderTest;

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    @Before
    public void setUp() {
        initMocks(this);
        sponseeServiceImplUnderTest = new SponseeServiceImpl(mockSponseeRepository, mockSponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository);
    }

    private AccountB2C getAccount(){
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setId(785L);
        accountB2C.setNumero("770000005");
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
    public void testSendSmsToSponsee() {
        // Setup
        final String msisdnSource = "770000005";
        final String msisdnDest = "770000006";

        Optional<Sponsee> optionalSponsee = Optional.of(getSponsee());
        when(mockSponseeRepository.findOneByMsisdn(anyString())).thenReturn(optionalSponsee);

        when(mockServicesOTP.generateMessage(any())).thenReturn(true);

        // Run the test
        sponseeServiceImplUnderTest.sendSmsToSponsee(msisdnSource, msisdnDest);

    }

    @Test(expected = ForbiddenException.class)
    public void testSendSmsToSponseeAlreadySponsored() {
        // Setup
        final String msisdnSource = "770000008";
        final String msisdnDest = "770000006";

        Optional<Sponsee> optionalSponsee = Optional.of(getSponsee());
        when(mockSponseeRepository.findOneByMsisdn(anyString())).thenReturn(optionalSponsee);

        // Run the test
        sponseeServiceImplUnderTest.sendSmsToSponsee(msisdnSource, msisdnDest);
    }

    @Test(expected = BadRequestAlertException.class)
    public void testSendSmsToSponseeWithNumberNoSponsored() {
        // Setup
        final String msisdnSource = "770000008";
        final String msisdnDest = "770000006";

        // Run the test
        sponseeServiceImplUnderTest.sendSmsToSponsee(msisdnSource, msisdnDest);
    }

    @Test(expected = BadRequestAlertException.class)
    public void testSendSmsToSponseeWithSmsNotSend() {
        // Setup
        final String msisdnSource = "770000005";
        final String msisdnDest = "770000006";

        Optional<Sponsee> optionalSponsee = Optional.of(getSponsee());
        when(mockSponseeRepository.findOneByMsisdn(anyString())).thenReturn(optionalSponsee);

        when(mockServicesOTP.generateMessage(any())).thenReturn(false);

        // Run the test
        sponseeServiceImplUnderTest.sendSmsToSponsee(msisdnSource, msisdnDest);

    }

    @Test
    public void testFindAllSponseeByMsisdn() {
        mockSponseeRepository = mock(SponseeRepository.class);
        accountB2CRepository = mock(AccountB2CRepository.class);
        sponseeServiceImplUnderTest = new SponseeServiceImpl(mockSponseeRepository, mockSponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository);
        Sponsee sponsee = new Sponsee();
        sponsee.setId(78L);
        sponsee.setMsisdn("778520000");
        List<Sponsee> sponseeList = new ArrayList<>();
        sponseeList.add(sponsee);

        AccountB2C b2C = new AccountB2C();
        b2C.setId(7L);
        b2C.setNumero("771326617");
        Optional<AccountB2C> b2COptional = Optional.of(b2C);

        when(mockSponseeRepository.findAllByAccountB2C(any())).thenReturn(sponseeList);
        when(accountB2CRepository.findOneByNumero(anyString())).thenReturn(b2COptional);

        List<Sponsee> allSponseeByMsisdn = sponseeServiceImplUnderTest.findAllSponseeBySponsor(b2C.getNumero());
        Assert.assertEquals(1,allSponseeByMsisdn.size());

    }
}
