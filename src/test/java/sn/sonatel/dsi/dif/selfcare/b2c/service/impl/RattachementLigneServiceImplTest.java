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
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.CaptchaService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.OfferBucket;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.*;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.CheckNumberFixVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLigneFixeVM;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 *
 * @author Bouya Kande
 * @since 1.1.4
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
    private CaptchaService mockCaptchaService;

    private RattachementLigneServiceImpl rattachementLigneServiceImpl;

    @Mock
    private CustomerOfferApiClient customerOfferApiClient;

    @Mock
    private  SelfcareSoapService selfcareSoapService;


    @Before
    public void setUp() {
        initMocks(this);
        rattachementLigneServiceImpl = new RattachementLigneServiceImpl(mockRattachementLigneRepository, mockAccountB2CRepository, mockCaptchaService, customerOfferApiClient, selfcareSoapService);
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

    @Test(expected = InvalidOrangeNumberException.class)
    public void testAddRattachementLigneFixeNoValid(){
        RattachementLigneFixeVM rattachementLigneFixeVM = new RattachementLigneFixeVM();
        rattachementLigneFixeVM.setIdClient("7895623");
        rattachementLigneFixeVM.setNumero("3389652523");
        rattachementLigneFixeVM.setLogin("777895666");
        rattachementLigneServiceImpl.addRattachementLigneFixe(rattachementLigneFixeVM);


    }

    @Test
    public void testAddRattachementLigneFixeSuccess(){
        //creation account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("778965536");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test536@gmail.com");
        accountB2C = mockAccountB2CRepository.save(accountB2C);

        String numeroclient = "841431";
        CustomerOffer customerOffer = getCustomer();
        customerOffer.setClientCode(numeroclient);

        ResponseEntity<CustomerOffer> response = ResponseEntity.ok(customerOffer);
        when(customerOfferApiClient.getCustomerOffer(anyString())).thenReturn(response);

        RattachementLigneFixeVM rattachementLigneFixeVM = new RattachementLigneFixeVM();
        rattachementLigneFixeVM.setIdClient(numeroclient);
        rattachementLigneFixeVM.setNumero("338366526");
        rattachementLigneFixeVM.setLogin(accountB2C.getNumero());
        rattachementLigneFixeVM.setTypeNumero(TypeNumero.FIXE);
        RattachementLigne ligne = rattachementLigneServiceImpl.addRattachementLigneFixe(rattachementLigneFixeVM);

        assertEquals(ligne.getNumero(), rattachementLigneFixeVM.getNumero());
        assertEquals(ligne.getIdClient(), rattachementLigneFixeVM.getIdClient());


    }

    @Test(expected = InvalidOrangeNumberException.class)
    public void testAddRattachementLigneFixeNoValidIdClient(){
        //creation account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("778965531");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test53@gmail.com");
        accountB2C = mockAccountB2CRepository.save(accountB2C);

        //create rattachement ligne
        RattachementLigne ligne = new RattachementLigne();
        ligne.setTypeNumero(TYPE_NUMERO_MOBILE);
        ligne.setNumero("770312323");
        ligne.setAccountB2C(accountB2C);
        mockRattachementLigneRepository.save(ligne);


        RattachementLigneFixeVM rattachementLigneFixeVM = new RattachementLigneFixeVM();
        rattachementLigneFixeVM.setIdClient("7895623");
        rattachementLigneFixeVM.setNumero("3389652523");
        rattachementLigneFixeVM.setLogin("777895666");
        rattachementLigneServiceImpl.addRattachementLigneFixe(rattachementLigneFixeVM);


    }

    @Test(expected = LigneAlreadyRattachedException.class)
    public void testAddRattachementLigneFixeeAlreadyRattached(){
        //creation account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("778965539");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test539@gmail.com");
        accountB2C = mockAccountB2CRepository.save(accountB2C);

        //create rattachement ligne
        RattachementLigne ligne = new RattachementLigne();
        ligne.setTypeNumero(TYPE_NUMERO_MOBILE);
        ligne.setNumero("339952323");
        ligne.setAccountB2C(accountB2C);
        mockRattachementLigneRepository.save(ligne);

        String numeroclient = "7895612";

        ResponseEntity<CustomerOffer> response = ResponseEntity.ok(getCustomer());
        when(customerOfferApiClient.getCustomerOffer(anyString())).thenReturn(response);

        RattachementLigneFixeVM rattachementLigneFixeVM = new RattachementLigneFixeVM();
        rattachementLigneFixeVM.setIdClient(numeroclient);
        rattachementLigneFixeVM.setNumero("339952323");
        rattachementLigneFixeVM.setLogin(accountB2C.getNumero());
        rattachementLigneServiceImpl.addRattachementLigneFixe(rattachementLigneFixeVM);


    }

    @Test(expected = AccountAlreadyHaveNumberFixeException.class)
    public void testAddRattachementLigneFixeAccountAlreadyHaveNumberFixe(){
        //creation account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("778965593");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test593@gmail.com");
        accountB2C = mockAccountB2CRepository.save(accountB2C);

        //create rattachement ligne
        RattachementLigne ligne = new RattachementLigne();
        ligne.setTypeNumero(TYPE_NUMERO_MOBILE);
        ligne.setNumero("338237244");
        ligne.setAccountB2C(accountB2C);
        mockRattachementLigneRepository.save(ligne);

        String numeroclient = "32722731";

        ResponseEntity<CustomerOffer> response = ResponseEntity.ok(getCustomer());
        when(customerOfferApiClient.getCustomerOffer(anyString())).thenReturn(response);

        RattachementLigneFixeVM rattachementLigneFixeVM = new RattachementLigneFixeVM();
        rattachementLigneFixeVM.setIdClient(numeroclient);
        rattachementLigneFixeVM.setNumero("339952356");
        rattachementLigneFixeVM.setLogin(accountB2C.getNumero());
        rattachementLigneServiceImpl.addRattachementLigneFixe(rattachementLigneFixeVM);


    }


    @Test(expected = LigneNotFoundException.class)
    public void testAddRattachementLigneFixeLigneNotFound(){

        String numeroclient = "32722731";

        ResponseEntity<CustomerOffer> response = ResponseEntity.ok(getCustomer());
        when(customerOfferApiClient.getCustomerOffer(anyString())).thenReturn(response);

        RattachementLigneFixeVM rattachementLigneFixeVM = new RattachementLigneFixeVM();
        rattachementLigneFixeVM.setIdClient(numeroclient);
        rattachementLigneFixeVM.setNumero("338237244");
        rattachementLigneFixeVM.setLogin("779562521");
        rattachementLigneFixeVM.setTypeNumero(TypeNumero.FIXE);
        RattachementLigne ligne = rattachementLigneServiceImpl.addRattachementLigneFixe(rattachementLigneFixeVM);


    }

    @Test
    public void testGetAccountB2CByIdClient(){

        String idClient = "0012707812";
        //creation account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("779965593");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test593@gmail.com");
        accountB2C = mockAccountB2CRepository.save(accountB2C);

        //create rattachement ligne
        RattachementLigne ligne = new RattachementLigne();
        ligne.setTypeNumero(TYPE_NUMERO_FIX);
        ligne.setNumero("338237244");
        ligne.setAccountB2C(accountB2C);
        ligne.setIdClient(idClient);
        mockRattachementLigneRepository.save(ligne);
        AccountB2C accountB2CByIdClient = rattachementLigneServiceImpl.getAccountB2CByIdClient(idClient);
        assertEquals(accountB2C.getNumero(),accountB2CByIdClient.getNumero());

    }

    @Test(expected = NumeroClientNotFoundException.class)
    public void testGetAccountB2CByIdClientNotFound(){

        String idClient = "1256305";

         rattachementLigneServiceImpl.getAccountB2CByIdClient(idClient);

    }

    private CustomerOffer getCustomer(){
        CustomerOffer customerOffer = new CustomerOffer();
        customerOffer.setClientCode("32722731");
        customerOffer.setCreateDate("2011-05-26T15:51:10");
        customerOffer.setEndUserId("338237244");
        customerOffer.setOfferCode("9131");
        customerOffer.setOfferType(OfferTypeEnum.PREPAID);
        customerOffer.setOfferStatus("ACTIF");
        customerOffer.setOfferName("Jamono New Scool");
        OfferBucket offerBucket = new OfferBucket();

        offerBucket.setUnit("sms");
        offerBucket.setValue("value");

        customerOffer.setData(offerBucket);



        return customerOffer;
    }

}
