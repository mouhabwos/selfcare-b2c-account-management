package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.*;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AccountB2CDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserInfoOuvertureCompte;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareUAAService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneAlreadyRattachedException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LoginAlreadyUsedException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.NumberRequest;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelfcareB2CApp.class})
public class AccountB2CServiceImplTest {

    private static final TypeNumero TYPE_NUMERO_MOBILE = TypeNumero.MOBILE;

    @Autowired
    private AccountB2CRepository mockAccountB2CRepository;
    @Autowired
    private RattachementLigneRepository mockRattachementLigneRepository;
    @Mock
    private SelfcareUAAService mockSelfcareUAAService;
    @Mock
    private MailService mockMailService;
    @Mock
    private DowloadManager mockDowloadManager;
    @Mock
    private CaptchaService mockCaptchaService;
    @Mock
    private BoosterManager boosterManager;

    private AccountB2CServiceImpl accountB2CServiceImplUnderTest;

    @Mock
    private SponseeRepository sponseeRepository;

    @Autowired
    private SponseeService sponseeService;

    @Before
    public void setUp() {
        initMocks(this);
        accountB2CServiceImplUnderTest = new AccountB2CServiceImpl(mockAccountB2CRepository, mockRattachementLigneRepository, mockSelfcareUAAService, mockMailService, mockDowloadManager,sponseeService,boosterManager);
    }

    private Optional<Sponsee> getSponsee(){
        Sponsee sponsee = new Sponsee();
        sponsee.setId(45L);
        sponsee.setMsisdn("775266364");
        Optional<Sponsee> optionalSponsee = Optional.of(sponsee);
        return optionalSponsee;
    }

    @Test
    public void testRegisterAccountB2CSucceess() {
        // Setup
       ManagedUserVM managedUserVM =  new ManagedUserVM();
        managedUserVM.setPassword("Passer12");
        managedUserVM.setLogin("775266364");
        managedUserVM.setLastName("lastname");
        managedUserVM.setFirstName("firstname");
        ResponseEntity response = ResponseEntity.status(HttpStatus.CREATED).build();

        when(mockSelfcareUAAService.regiserAccount(managedUserVM)).thenReturn(response);
        when(sponseeRepository.findOneByMsisdn(anyString())).thenReturn(getSponsee());

        // Run the test
         AccountB2C result = accountB2CServiceImplUnderTest.registerAccountB2C(managedUserVM);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testRegisterAccountB2CEmailNotNull() {
        // Setup
        ManagedUserVM managedUserVM =  new ManagedUserVM();
        managedUserVM.setPassword("Passer12");
        managedUserVM.setLogin("775266363");
        managedUserVM.setEmail("testmail@gmail.com");
        managedUserVM.setLastName("lastname");
        managedUserVM.setFirstName("firstname");
        ResponseEntity response = ResponseEntity.status(HttpStatus.CREATED).build();

        when(mockSelfcareUAAService.regiserAccount(managedUserVM)).thenReturn(response);
        AccountB2C expectedResult = new AccountB2C();

        // Run the test
        AccountB2C result = accountB2CServiceImplUnderTest.registerAccountB2C(managedUserVM);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testRegisterAccountB2CFailed() {
        // Setup
        ManagedUserVM managedUserVM =  new ManagedUserVM();
        managedUserVM.setPassword("Passer12");
        managedUserVM.setLogin("775266378");
        managedUserVM.setEmail("testmail@gmail.com");
        managedUserVM.setLastName("lastname");
        managedUserVM.setFirstName("firstname");
        ResponseEntity response = ResponseEntity.status(HttpStatus.CREATED).build();

        when(mockSelfcareUAAService.regiserAccount(managedUserVM)).thenReturn(response);
        // Run the test
        accountB2CServiceImplUnderTest.registerAccountB2C(managedUserVM);

    }


    @Test(expected = LoginAlreadyUsedException.class)
    public void testRegisterAccountB2CNumberAlreadyUsed() {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("lastname");
        accountB2C.setFirstName("firstname");
        accountB2C.setNumero("778525255");
         mockAccountB2CRepository.save(accountB2C);

        ManagedUserVM managedUserVM =  new ManagedUserVM();
        managedUserVM.setPassword("Passer12");
        managedUserVM.setLogin("778525255");
        managedUserVM.setLastName("lastname");
        managedUserVM.setFirstName("firstname");
        accountB2CServiceImplUnderTest.registerAccountB2C(managedUserVM);

    }

    @Test(expected = LigneAlreadyRattachedException.class)
    public void testRegisterAccountB2CNumberAlreadyRattached() {

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("lastname");
        accountB2C.setFirstName("firstname");
        accountB2C.setNumero("778525255");
         accountB2C = mockAccountB2CRepository.save(accountB2C);

        RattachementLigne  ligne = new RattachementLigne();
        ligne.setNumero("774232024");
        ligne.setAccountB2C(accountB2C);
        ligne.setTypeNumero(TYPE_NUMERO_MOBILE);
        mockRattachementLigneRepository.save(ligne);

        ManagedUserVM managedUserVM =  new ManagedUserVM();
        managedUserVM.setPassword("Passer12");
        managedUserVM.setLogin(ligne.getNumero());
        managedUserVM.setLastName("lastname");
        managedUserVM.setFirstName("firstname");
        accountB2CServiceImplUnderTest.registerAccountB2C(managedUserVM);
    }

    @Test
    public void testUpdateAccountB2C() {
        // Setup
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("lastname");
        accountB2C.setFirstName("firstname");
        accountB2C.setNumero("778525252");
      accountB2C =  mockAccountB2CRepository.save(accountB2C);
        AccountB2CDTO accountB2CDTO = new AccountB2CDTO();
        accountB2CDTO.setId(accountB2C.getId());
        accountB2CDTO.setLastName("lastname");
        accountB2CDTO.setFirstName("change");
        accountB2CDTO.setNumero("778525252");
        final AccountB2C expectedResult = null;

        // Run the test
       AccountB2C result = accountB2CServiceImplUnderTest.updateAccountB2C(accountB2CDTO);

       String change = "change";

        // Verify the results
        assertEquals(result.getFirstName(), change);
    }

    @Test
    public void testEmailExistingVerify() {
        // Setup
        final String email = "email";

        // Run the test
        final boolean result = accountB2CServiceImplUnderTest.emailExistingVerify(email);

        // Verify the results

    }

    @Test
    public void testSendmail() {
        // Setup
        final UserInfoOuvertureCompte b2C = null;

        // Run the test
        accountB2CServiceImplUnderTest.sendmail(b2C);

        // Verify the results
    }

    @Test
    public void testCheckNumber(){

        String msisdn = "779455656";
        when(mockCaptchaService.verifyCaptcha(anyString())).thenReturn(true);
        NumberRequest request = new NumberRequest();
        request.setMsisdn(msisdn);
        request.setToken("kbjidfbicnokfljvregjnsfouhrfbdsfoozfboufhzofhhzlzofoodfojzfzojfbzjofbzojfzjfoz");
        accountB2CServiceImplUnderTest.checkNumber(request);

    }

    @Test(expected = LoginAlreadyUsedException.class)
    public void testCheckNumberUsingAccount(){

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("lastname");
        accountB2C.setFirstName("firstname");
        accountB2C.setNumero("778525252");
        accountB2C =  mockAccountB2CRepository.save(accountB2C);

        when(mockCaptchaService.verifyCaptcha(anyString())).thenReturn(true);
        NumberRequest request = new NumberRequest();
        request.setMsisdn(accountB2C.getNumero());
        request.setToken("kbjidfbicnokfljvregjnsfouhrfbdsfoozfboufhzofhhzlzofoodfojzfzojfbzjofbzojfzjfoz");
        accountB2CServiceImplUnderTest.checkNumber(request);

    }

    @Test(expected = LigneAlreadyRattachedException.class)
    public void testCheckNumberUsingRattachement(){

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("lastname");
        accountB2C.setFirstName("firstname");
        accountB2C.setNumero("778525250");
        accountB2C =  mockAccountB2CRepository.save(accountB2C);

        RattachementLigne ligne = new RattachementLigne();
        ligne.setNumero("774232020");
        ligne.setAccountB2C(accountB2C);
        ligne.setTypeNumero(TYPE_NUMERO_MOBILE);
        mockRattachementLigneRepository.save(ligne);

        when(mockCaptchaService.verifyCaptcha(anyString())).thenReturn(true);
        NumberRequest request = new NumberRequest();
        request.setMsisdn(ligne.getNumero());
        request.setToken("kbjidfbicnokfljvregjnsfouhrfbdsfoozfboufhzofhhzlzofoodfojzfzojfbzjofbzojfzjfoz");
        accountB2CServiceImplUnderTest.checkNumber(request);

    }


}
