package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

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
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.SponseeMapper;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ForbiddenException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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

    @Before
    public void setUp() {
        initMocks(this);
        sponseeServiceImplUnderTest = new SponseeServiceImpl(mockSponseeRepository, mockSponseeMapper, mockApplicationProperties, mockServicesOTP);
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
}
