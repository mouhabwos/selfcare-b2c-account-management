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
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponsorRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.SponseeMapper;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ForbiddenException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneAlreadyRattachedException;

import java.util.ArrayList;
import java.util.List;
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


    @Autowired
    private SponseeRepository sponseeRepository;

    @Autowired
    private SponseeMapper sponseeMapper;

    @Before
    public void setUp() {
        initMocks(this);
        sponseeServiceImplUnderTest = new SponseeServiceImpl(mockSponseeRepository, mockSponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository, rattachementLigneRepository);
    }

    public void forMockService() {

        sponseeServiceImplUnderTest = new SponseeServiceImpl(mockSponseeRepository, mockSponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository, rattachementLigneRepository);
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

    @Test
    public void testFindAllSponseeByMsisdn() {
        mockSponseeRepository = mock(SponseeRepository.class);
        accountB2CRepository = mock(AccountB2CRepository.class);
        sponseeServiceImplUnderTest = new SponseeServiceImpl(mockSponseeRepository, mockSponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository,rattachementLigneRepository);
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

     @Test(expected = BadRequestAlertException.class)
    public void testRegisterSponseeWithSponsorNoAccount(){
        AccountB2C accountB2C = getAccount();
        accountB2C.setId(null);
        accountB2CRepository.save(accountB2C);

        SponseeDTO sponseeDTO = getSponseeDTO();
        sponseeDTO.setMsisdnSponsor("770100000");

        sponseeServiceImplUnderTest.register(sponseeDTO);

    }

    @Test(expected = BadRequestAlertException.class)
    public void testRegisterSponseeWithNumAlreadyUsed(){

        accountB2CRepository = mock(AccountB2CRepository.class);
        sponseeServiceImplUnderTest = new SponseeServiceImpl(mockSponseeRepository, mockSponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository, rattachementLigneRepository);


       AccountB2C accountB2C = getAccount();
        String msisdnSponsor = getSponsor().get().getMsisdn();
        accountB2C.setNumero(msisdnSponsor);
        Optional<AccountB2C>  b2COptional = Optional.of(accountB2C);
        when(accountB2CRepository.findOneByNumero(anyString())).thenReturn(b2COptional);


        SponseeDTO sponseeDTO = getSponseeDTO();
        sponseeDTO.setMsisdn("");
        sponseeDTO.setMsisdn("770000006");
        sponseeDTO.setMsisdnSponsor(accountB2C.getNumero());

        sponseeServiceImplUnderTest.register(sponseeDTO);

    }

    @Test(expected = BadRequestAlertException.class)
    public void testRegisterSponseeAlreadyRattachedException(){

        sponseeServiceImplUnderTest = new SponseeServiceImpl(sponseeRepository, sponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository, rattachementLigneRepository);


        // -------- save sponsor
        Optional<Sponsor> sponsorOptional = getSponsor();
        Sponsor sponsor = sponsorOptional.get();


        // rattachement ligne
        RattachementLigne rattachementLigne = new RattachementLigne();
        rattachementLigne.setNumero(sponsor.getMsisdn());
        rattachementLigne.setAccountB2C(null);
        rattachementLigne.setTypeNumero(TypeNumero.MOBILE);
        RattachementLigne ligne = rattachementLigneRepository.save(rattachementLigne);

        // -------- sponseeDTO add value
        SponseeDTO sponseeDTO = getSponseeDTO();
        sponseeDTO.setMsisdnSponsor(ligne.getNumero());
        sponseeDTO.setMsisdn(ligne.getNumero());

        sponseeServiceImplUnderTest.register(sponseeDTO);


    }



    @Test
    public void testUpdateSponsee(){

        sponseeServiceImplUnderTest = new SponseeServiceImpl(sponseeRepository, sponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository, rattachementLigneRepository);

        //save sponsor
        Optional<Sponsor> sponsorOptional = getSponsor();
        Sponsor sponsor = sponsorOptional.get();
        //save account sponsor
        AccountB2C accountB2C = getAccount();
        accountB2C.setId(null);
        accountB2C.setNumero(sponsor.getMsisdn());
        AccountB2C save = accountB2CRepository.save(getAccount());
        // save sponsee
        SponseeDTO sponseeDTO = getSponseeDTO();
        Sponsee sponsee = new Sponsee();
        sponsee.setAccountB2C(save);
        sponsee.setMsisdn(sponseeDTO.getMsisdn());
        sponsee.setFirstName(sponseeDTO.getFirstName());
        Sponsee saveSponsee = sponseeRepository.save(sponsee);
        sponseeDTO.setMsisdnSponsor(save.getNumero());
        sponseeDTO.setLastName("hello");
        sponseeDTO.setFirstName("hello firstname");

        SponseeDTO update = sponseeServiceImplUnderTest.update(sponseeDTO);

        Assert.assertEquals(sponseeDTO.getMsisdnSponsor(),update.getMsisdnSponsor());
        Assert.assertEquals(sponseeDTO.getFirstName(),update.getFirstName());
        Assert.assertEquals(sponseeDTO.getLastName(),update.getLastName());
        Assert.assertEquals(sponseeDTO.getCreatedDate(),update.getCreatedDate());


    }

    @Test
    public void testGetSponseeById(){

        sponseeServiceImplUnderTest = new SponseeServiceImpl(sponseeRepository, sponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository, rattachementLigneRepository);

        // -------- save sponsor
        Optional<Sponsor> sponsorOptional = getSponsor();
        Sponsor sponsor = sponsorOptional.get();

        // -------- save account sponsor
        AccountB2C accountB2C = getAccount();

        accountB2C.setNumero(sponsor.getMsisdn());
        Optional<AccountB2C> accountB2COptional = Optional.of(accountB2C);
        AccountB2C saveAccount = accountB2CRepository.save(accountB2C);

        // save sponsee
        SponseeDTO sponseeDTO = getSponseeDTO();
        Sponsee sponsee = new Sponsee();
        sponsee.setAccountB2C(saveAccount);
        sponsee.setMsisdn(sponseeDTO.getMsisdn());
        sponsee.setFirstName(sponseeDTO.getFirstName());
        Sponsee saveSponsee = sponseeRepository.save(sponsee);

        Optional<SponseeDTO> one = sponseeServiceImplUnderTest.findOne(saveSponsee.getId());

        Assert.assertEquals(sponsee.getAccountB2C().getNumero(),one.get().getMsisdnSponsor());
        Assert.assertEquals(sponsee.getFirstName(),one.get().getFirstName());
        Assert.assertEquals(sponsee.getLastName(),one.get().getLastName());
        Assert.assertEquals(sponsee.getCreatedDate(),one.get().getCreatedDate());

    }

    @Test(expected = LigneAlreadyRattachedException.class)
    public void testRegisterSponseeWithExceptionAlreadyRattached(){

        rattachementLigneRepository = mock(RattachementLigneRepository.class);
        sponseeServiceImplUnderTest = new SponseeServiceImpl(sponseeRepository, sponseeMapper, mockApplicationProperties, mockServicesOTP, accountB2CRepository, rattachementLigneRepository);


        // -------- save sponsor
        Optional<Sponsor> sponsorOptional = getSponsor();
        Sponsor sponsor = sponsorOptional.get();

        // rattachement ligne
        RattachementLigne rattachementLigne = new RattachementLigne();
        rattachementLigne.setNumero("779999999");
        rattachementLigne.setAccountB2C(null);
        rattachementLigne.setTypeNumero(TypeNumero.MOBILE);
        when(rattachementLigneRepository.findByNumero(anyString())).thenReturn(Optional.of(rattachementLigne));

        // -------- sponseeDTO add value
        SponseeDTO sponseeDTO = getSponseeDTO();
        sponseeDTO.setMsisdnSponsor(rattachementLigne.getNumero());
        sponseeDTO.setMsisdn(rattachementLigne.getNumero());

        sponseeServiceImplUnderTest.register(sponseeDTO);

    }




}
