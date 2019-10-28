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
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponsorRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.SponseeMapper;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ForbiddenException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    @Autowired
    private RattachementLigneRepository rattachementLigneRepository;

    @Mock
    private SponsorRepository sponsorRepository;

    @Before
    public void setUp() {
        initMocks(this);
        sponseeServiceImplUnderTest = new SponseeServiceImpl(mockSponseeRepository, mockSponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository, rattachementLigneRepository, sponsorRepository);
    }

    public void forMockService() {

        sponseeServiceImplUnderTest = new SponseeServiceImpl(mockSponseeRepository, mockSponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository, rattachementLigneRepository, sponsorRepository);
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

    private SponseeDTO getSponseeDTO(){

        SponseeDTO sponsee = new SponseeDTO();
        sponsee.setMsisdn("770000006");

        return sponsee;
    }

    private Optional<Sponsor> getSponsor(){
        Sponsor sponsor = new Sponsor();
        sponsor.setMsisdn("77000 00 00");
        sponsor.setMatricule("mat");
        sponsor.setFirstName("");
        sponsor.setLastName("");
        return Optional.of(sponsor);
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

    @Test(expected = ForbiddenException.class)
    public void testRegisterSponseeWithForbidden(){

        AccountB2C accountB2C = getAccount();
        accountB2C.setId(null);
        accountB2CRepository.save(accountB2C);

        SponseeDTO sponseeDTO = getSponseeDTO();
        sponseeDTO.setMsisdnSponsor("77900 00 00");

        sponseeServiceImplUnderTest.register(sponseeDTO);

    }

    @Test(expected = BadRequestAlertException.class)
    public void testRegisterSponseeWithSponsorNoAccount(){

        AccountB2C accountB2C = getAccount();
        accountB2C.setId(null);
        accountB2CRepository.save(accountB2C);

        when(sponsorRepository.findOneByMsisdn(anyString())).thenReturn(getSponsor());

        SponseeDTO sponseeDTO = getSponseeDTO();
        sponseeDTO.setMsisdnSponsor("770100000");

        sponseeServiceImplUnderTest.register(sponseeDTO);

    }

    @Test(expected = BadRequestAlertException.class)
    public void testRegisterSponseeWithNumAlreadyUsed(){

        accountB2CRepository = mock(AccountB2CRepository.class);
        forMockService();
        AccountB2C accountB2C = getAccount();
        String msisdnSponsor = getSponsor().get().getMsisdn();
        accountB2C.setNumero(msisdnSponsor);
        Optional<AccountB2C>  b2COptional = Optional.of(accountB2C);
        when(accountB2CRepository.findOneByNumero(anyString())).thenReturn(b2COptional);
       // AccountB2C save = accountB2CRepository.save(accountB2C);

        when(sponsorRepository.findOneByMsisdn(anyString())).thenReturn(getSponsor());

        SponseeDTO sponseeDTO = getSponseeDTO();
        sponseeDTO.setMsisdn("");
        sponseeDTO.setMsisdn("770000006");
        sponseeDTO.setMsisdnSponsor(accountB2C.getNumero());

        sponseeServiceImplUnderTest.register(sponseeDTO);

    }

    @Test
    public void testRegisterSponsee(){

       accountB2CRepository = mock(AccountB2CRepository.class);

        AccountB2C accountB2C = getAccount();
        String msisdnSponsor = getSponsor().get().getMsisdn();
        accountB2C.setNumero(msisdnSponsor);
        Optional<AccountB2C>  b2COptional = Optional.of(accountB2C);
        when(accountB2CRepository.findOneByNumero(anyString())).thenReturn(b2COptional);
        // AccountB2C save = accountB2CRepository.save(accountB2C);

        when(sponsorRepository.findOneByMsisdn(anyString())).thenReturn(getSponsor());

        SponseeDTO sponseeDTO = getSponseeDTO();

        sponseeDTO.setMsisdn("77 666 66 66");

        sponseeDTO.setMsisdnSponsor(accountB2C.getNumero());

        sponseeServiceImplUnderTest.register(sponseeDTO);

    }
}
