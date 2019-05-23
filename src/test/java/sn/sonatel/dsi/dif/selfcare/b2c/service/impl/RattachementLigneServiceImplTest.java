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
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.CaptchaService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.AccountAlreadyHaveNumberFixeException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneAlreadyRattachedException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.NoValideNumberFixeException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.CheckNumberFixVM;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 *
 * @since 23/05/2019
 * @author Bouya Kande
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelfcareB2CApp.class})
public class RattachementLigneServiceImplTest {

    private static final TypeNumero TYPE_NUMERO_MOBILE = TypeNumero.MOBILE;

    private static final TypeNumero TYPE_NUMERO_FIX = TypeNumero.FIXE;

    @Autowired
    private RattachementLigneRepository mockRattachementLigneRepository;
    @Autowired
    private AccountB2CRepository mockAccountB2CRepository;
    @Mock
    private SelfcareSoapService mockSelfcareSoapService;
    @Mock
    private CaptchaService mockCaptchaService;

    private RattachementLigneServiceImpl rattachementLigneServiceImpl;

    @Before
    public void setUp() {
        initMocks(this);
        rattachementLigneServiceImpl = new RattachementLigneServiceImpl(mockRattachementLigneRepository, mockAccountB2CRepository, mockSelfcareSoapService, mockCaptchaService);
    }


    @Test
    public void testCheckNumberFixSucess() {

        //creation account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("778965335");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test35@gmail.com");
        accountB2C = mockAccountB2CRepository.save(accountB2C);

        //create rattachement ligne
        RattachementLigne ligne = new RattachementLigne();
        ligne.setTypeNumero(TYPE_NUMERO_FIX);
        ligne.setNumero("339962289");
        ligne.setAccountB2C(accountB2C);
        mockRattachementLigneRepository.save(ligne);


        CheckNumberFixVM numberRequest = new CheckNumberFixVM();
        numberRequest.setLogin("339962289");
        numberRequest.setMsisdn("339962217");
        numberRequest.setToken("ndskdnslkdnksqnkjjezkjzebdlndlknqsozeoinzlkndlzknjnlsnflknkzlkzehfzebf");

        when(mockCaptchaService.verifyCaptcha(anyString())).thenReturn(true);

        ResponseEntity responseEntity = rattachementLigneServiceImpl.checkNumberFix(numberRequest);
        System.out.println(responseEntity);
    }


    @Test
    public void testCheckNumberFixCapchaInvalid() {
        // Setup
       CheckNumberFixVM numberRequest = new CheckNumberFixVM();
       numberRequest.setLogin("774562323");
       numberRequest.setMsisdn("339962218");
       numberRequest.setToken("ndskdnslkdnksqnkjjezkjzebdlndlknqsozeoinzlkndlzknjnlsnflknkzlkzehfzebf");
       when(mockCaptchaService.verifyCaptcha(anyString())).thenReturn(false);
       ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       ResponseEntity responseEntity = rattachementLigneServiceImpl.checkNumberFix(numberRequest);
        assertEquals(expectedResult, responseEntity);

    }

    @Test(expected = NoValideNumberFixeException.class)
    public void testCheckNumberFixInvalidNumberOrange() {

        CheckNumberFixVM numberRequest = new CheckNumberFixVM();
        numberRequest.setLogin("774562323");
        numberRequest.setMsisdn("3399622188");
        numberRequest.setToken("ndskdnslkdnksqnkjjezkjzebdlndlknqsozeoinzlkndlzknjnlsnflknkzlkzehfzebf");
        when(mockCaptchaService.verifyCaptcha(anyString())).thenReturn(true);
        rattachementLigneServiceImpl.checkNumberFix(numberRequest);

    }

    @Test (expected = LigneAlreadyRattachedException.class)
    public void testCheckNumberFixLigneAlreadyRattached() {

        //creation account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("778965351");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test778@gmail.com");
        accountB2C = mockAccountB2CRepository.save(accountB2C);

        //create rattachement ligne
        RattachementLigne ligne = new RattachementLigne();
        ligne.setTypeNumero(TYPE_NUMERO_FIX);
        ligne.setNumero("339962218");
        ligne.setAccountB2C(accountB2C);
        mockRattachementLigneRepository.save(ligne);

        CheckNumberFixVM numberRequest = new CheckNumberFixVM();
        numberRequest.setLogin("774562323");
        numberRequest.setMsisdn("33 996 22 18");
        numberRequest.setToken("ndskdnslkdnksqnkjjezkjzebdlndlknqsozeoinzlkndlzknjnlsnflknkzlkzehfzebf");
        when(mockCaptchaService.verifyCaptcha(anyString())).thenReturn(true);
        ResponseEntity responseEntity = rattachementLigneServiceImpl.checkNumberFix(numberRequest);


    }

    @Test (expected = AccountAlreadyHaveNumberFixeException.class)
    public void testCheckNumberFixWithAccountAlreadyHaveNumFix() {

        //creation account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("778965353");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test3@gmail.com");
        accountB2C = mockAccountB2CRepository.save(accountB2C);

        //create rattachement ligne
        RattachementLigne ligne = new RattachementLigne();
        ligne.setTypeNumero(TYPE_NUMERO_FIX);
        ligne.setNumero("339962215");
        ligne.setAccountB2C(accountB2C);
        mockRattachementLigneRepository.save(ligne);


        CheckNumberFixVM numberRequest = new CheckNumberFixVM();
        numberRequest.setLogin(accountB2C.getNumero());
        numberRequest.setMsisdn("339962217");
        numberRequest.setToken("ndskdnslkdnksqnkjjezkjzebdlndlknqsozeoinzlkndlzknjnlsnflknkzlkzehfzebf");

        when(mockCaptchaService.verifyCaptcha(anyString())).thenReturn(true);

        ResponseEntity responseEntity = rattachementLigneServiceImpl.checkNumberFix(numberRequest);
        System.out.println(responseEntity);

    }


}
