package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.ClientType;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AbonneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponseeService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.api.CustomerOfferApiClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.OfferBucket;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.InfoClientWrapper;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareOTPService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.InvalidOrangeNumberException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.NotFoundNumberException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.InfoNumberVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLigneCNIVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLigneFixeVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLigneVM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Bouya Kande
 * @since 1.1.4
 */


@RunWith(SpringRunner.class)
@IntegrationTest
public class RattachementLigneServiceImplTest {

    private static final TypeNumero TYPE_NUMERO_MOBILE = TypeNumero.MOBILE;

    private static final TypeNumero TYPE_NUMERO_FIX = TypeNumero.FIXE;

    private static final String RESPONSE_NUMERO_ORGANIZATION = "{\n" +
        "    \"clientType\": \"ORGANIZATION\",\n" +
        "    \"information\": {\n" +
        "        \"id\": null,\n" +
        "        \"contactNumbers\": [],\n" +
        "        \"givenName\": null,\n" +
        "        \"familyName\": null,\n" +
        "        \"birthDate\": null,\n" +
        "        \"title\": null,\n" +
        "        \"status\": null,\n" +
        "        \"gender\": null,\n" +
        "        \"maritalStatus\": null,\n" +
        "        \"type\": null,\n" +
        "        \"individualIdentification\": []\n" +
        "    },\n" +
        "    \"organization\": {\n" +
        "        \"id\": \"782363572\",\n" +
        "        \"href\": null,\n" +
        "        \"isLegalEntity\": null,\n" +
        "        \"type\": null,\n" +
        "        \"tradingName\": \"SONATEL MOBILES\",\n" +
        "        \"nameType\": null,\n" +
        "        \"status\": \"ACTIF\",\n" +
        "        \"organizationIdentification\": [\n" +
        "            {\n" +
        "                \"id\": null,\n" +
        "                \"type\": \"COMPANYREGISTRATIONNUMBER\",\n" +
        "                \"identificationId\": \"SNDKR1999B1877\",\n" +
        "                \"issuingAuthority\": null,\n" +
        "                \"href\": null,\n" +
        "                \"issuingDate\": \"2007-02-08T12:00:00\",\n" +
        "                \"expiryDate\": null\n" +
        "            }\n" +
        "        ]\n" +
        "    }\n" +
        "}";

    private static final String RESPONSE_NUMERO_INDIVIDUAL = "{\n" +
        "    \"clientType\": \"INDIVIDUAL\",\n" +
        "    \"information\": {\n" +
        "        \"id\": \"781040956\",\n" +
        "        \"contactNumbers\": [" +
        "        ],\n" +
        "        \"givenName\": \"TEST\",\n" +
        "        \"familyName\": \"TEST\",\n" +
        "        \"birthDate\": \"1000-00-00\",\n" +
        "        \"title\": \"MME\",\n" +
        "        \"status\": \"ACTIF\",\n" +
        "        \"gender\": \"FEMALE\",\n" +
        "        \"maritalStatus\": null,\n" +
        "        \"type\": null,\n" +
        "        \"individualIdentification\": [\n" +
        "            {\n" +
        "                \"id\": null,\n" +
        "                \"type\": \"IDENTITYCARD\",\n" +
        "                \"identificationId\": \"000000060000\",\n" +
        "                \"issuingAuthority\": null,\n" +
        "                \"href\": null,\n" +
        "                \"issuingDate\": null,\n" +
        "                \"expiryDate\": null\n" +
        "            }\n" +
        "        ]\n" +
        "    },\n" +
        "    \"organization\": {\n" +
        "        \"id\": null,\n" +
        "        \"href\": null,\n" +
        "        \"isLegalEntity\": null,\n" +
        "        \"type\": null,\n" +
        "        \"tradingName\": null,\n" +
        "        \"nameType\": null,\n" +
        "        \"status\": null,\n" +
        "        \"organizationIdentification\": []\n" +
        "    }\n" +
        "}";


    @Autowired
    private RattachementLigneRepository mockRattachementLigneRepository;
    @Autowired
    private AccountB2CRepository mockAccountB2CRepository;

    private RattachementLigneServiceImpl rattachementLigneServiceImpl;

    @Mock
    private CustomerOfferApiClient customerOfferApiClient;


    @Mock
    private SponseeRepository sponseeRepository;

    @Autowired
    private SponseeService sponseeService;

    @Mock
    private AbonneService abonneService;
    @Mock
    private SelfcareOTPService selfcareOTPService;

    @Before
    public void setUp() {
        initMocks(this);
        rattachementLigneServiceImpl = new RattachementLigneServiceImpl(mockRattachementLigneRepository, mockAccountB2CRepository, customerOfferApiClient, sponseeService, abonneService, selfcareOTPService);
    }


    /*@Test
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

    }*/

    @Test
    public void testGetRattachementLignes() {

        // Setup
        String msisdn = "77000 00 00";
        mockAccountB2CRepository = mock(AccountB2CRepository.class);
        mockRattachementLigneRepository = mock(RattachementLigneRepository.class);
        rattachementLigneServiceImpl = new RattachementLigneServiceImpl(mockRattachementLigneRepository, mockAccountB2CRepository, customerOfferApiClient, sponseeService, abonneService, selfcareOTPService);

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("");
        accountB2C.setFirstName("");
        accountB2C.setNumero(msisdn);
        Optional<AccountB2C> b2COptional = Optional.of(accountB2C);
        when(mockAccountB2CRepository.findOneByNumero(anyString())).thenReturn(b2COptional);
        List<RattachementLigne> rattachementLignes = new ArrayList<>();
        when(mockRattachementLigneRepository.findAllByAccountB2C(any())).thenReturn(rattachementLignes);
        ResponseEntity<CustomerOffer> response = ResponseEntity.ok(getCustomer());
        when(customerOfferApiClient.getCustomerOffer(anyString())).thenReturn(response);

        // Run the test
        final List<InfoNumberVM> result = rattachementLigneServiceImpl.getRattachementLignes(msisdn, true);

        List<InfoNumberVM> expectedResult = new ArrayList<>();
        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetRattachementLignesWithNullBodyCustomerOffer() {

        // Setup
        String msisdn = "77000 00 00";
        mockAccountB2CRepository = mock(AccountB2CRepository.class);
        mockRattachementLigneRepository = mock(RattachementLigneRepository.class);
        rattachementLigneServiceImpl = new RattachementLigneServiceImpl(mockRattachementLigneRepository, mockAccountB2CRepository, customerOfferApiClient, sponseeService, abonneService, selfcareOTPService);

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("");
        accountB2C.setFirstName("");
        accountB2C.setNumero(msisdn);
        Optional<AccountB2C> b2COptional = Optional.of(accountB2C);
        when(mockAccountB2CRepository.findOneByNumero(anyString())).thenReturn(b2COptional);
        List<RattachementLigne> rattachementLignes = new ArrayList<>();
        when(mockRattachementLigneRepository.findAllByAccountB2C(any())).thenReturn(rattachementLignes);
        when(customerOfferApiClient.getCustomerOffer("msisdn")).thenReturn(null);

        // Run the test
        final List<InfoNumberVM> result = rattachementLigneServiceImpl.getRattachementLignes(msisdn, true);

        List<InfoNumberVM> expectedResult = new ArrayList<>();
        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetRattachementLignesWithNullOfferTypeAndOfferName() {

        // Setup
        String msisdn = "77000 00 00";

        mockAccountB2CRepository = mock(AccountB2CRepository.class);
        mockRattachementLigneRepository = mock(RattachementLigneRepository.class);
        rattachementLigneServiceImpl = new RattachementLigneServiceImpl(mockRattachementLigneRepository, mockAccountB2CRepository, customerOfferApiClient, sponseeService, abonneService, selfcareOTPService);

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setLastName("");
        accountB2C.setFirstName("");
        accountB2C.setNumero(msisdn);

        Optional<AccountB2C> b2COptional = Optional.of(accountB2C);
        when(mockAccountB2CRepository.findOneByNumero(anyString())).thenReturn(b2COptional);

        RattachementLigne ligne = new RattachementLigne();
        ligne.setAccountB2C(accountB2C);
        ligne.setNumero("330000000");
        ligne.setTypeNumero(TypeNumero.FIXE);
        List<RattachementLigne> rattachementLignes = new ArrayList<>();
        rattachementLignes.add(ligne);
        when(mockRattachementLigneRepository.findAllByAccountB2C(any())).thenReturn(rattachementLignes);

        CustomerOffer customerOffer = new CustomerOffer();

        when(customerOfferApiClient.getCustomerOffer(anyString())).thenReturn(ResponseEntity.ok(customerOffer));

        // Run the test
        final List<InfoNumberVM> result = rattachementLigneServiceImpl.getRattachementLignes(msisdn, true);

        // Verify the results
        assertEquals(1, result.size());
        assertEquals("", result.get(0).getFormule());
        assertEquals("", result.get(0).getProfil());
    }

    private CustomerOffer getCustomer() {
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

    @Test(expected = InvalidOrangeNumberException.class)
    public void testAddRattachementLigneFixeNoValid() {
        //creation account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("778965351");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test778@gmail.com");

        //create rattachement ligne
        RattachementLigne ligne = new RattachementLigne();
        ligne.setTypeNumero(TYPE_NUMERO_FIX);
        ligne.setNumero("339962218");
        ligne.setAccountB2C(accountB2C);
        RattachementLigneVM ligneVM = new RattachementLigneVM();
        ligneVM.setLogin(accountB2C.getNumero());
        ligneVM.setNumero("77123202000");
        rattachementLigneServiceImpl.addRattachementLigne(ligneVM);


    }

    @Test
    public void testEquals() {

        IndividualInformation information = new IndividualInformation();

        int hashCode = information.hashCode();
        assertNotNull(hashCode);

    }

    @Test
    public void testRegisterRattachementLigneFixe() {

        String codeClient = "32722731";
        String numFixe = "338237244";
        String login = "770502323";

        // sauvegarde user in Account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero(login);
        accountB2C.setFirstName("hello");
        accountB2C.setLastName("hello");
        AccountB2C saveAccount = mockAccountB2CRepository.save(accountB2C);

        //Mock response api get CustomerOffer
        CustomerOffer customerOffer = getCustomer();
        customerOffer.setClientCode(codeClient);

        ResponseEntity responseEntity = ResponseEntity.ok(customerOffer);
        when(customerOfferApiClient.getCustomerOffer(anyString())).thenReturn(responseEntity);

        InfoClientWrapper infoClientWrapper = new InfoClientWrapper();
        infoClientWrapper.setClientType(ClientType.INDIVIDUAL);
        when(abonneService.getInformations(anyString())).thenReturn(infoClientWrapper);

        //call register ligne fixe
        RattachementLigneFixeVM fixeVM = new RattachementLigneFixeVM();
        fixeVM.setIdClient(customerOffer.getClientCode());
        fixeVM.setLogin(login);
        fixeVM.setNumero(numFixe);
        fixeVM.setTypeNumero(TypeNumero.FIXE);

        RattachementLigne ligneResponse = rattachementLigneServiceImpl.addRattachementLigneFixe(fixeVM);

        assertTrue(ligneResponse != null);

        Assert.assertEquals(fixeVM.getNumero(), ligneResponse.getNumero());


    }


    @Test(expected = BadRequestAlertException.class)
    public void testRegisterRattachementLigneFixeBadIdClient() {

        String codeClient = "3272889966666";
        String numFixe = "338237244";
        String login = "770502323";

        // sauvegarde user in Account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero(login);
        accountB2C.setFirstName("hello");
        accountB2C.setLastName("hello");
        AccountB2C saveAccount = mockAccountB2CRepository.save(accountB2C);

        InfoClientWrapper infoClientWrapper = new InfoClientWrapper();
        infoClientWrapper.setClientType(ClientType.INDIVIDUAL);
        when(abonneService.getInformations(anyString())).thenReturn(infoClientWrapper);

        //Mock response api get CustomerOffer
        CustomerOffer customerOffer = getCustomer();

        ResponseEntity responseEntity = ResponseEntity.ok(customerOffer);

        when(customerOfferApiClient.getCustomerOffer(anyString())).thenReturn(responseEntity);


        //call register ligne fixe
        RattachementLigneFixeVM fixeVM = new RattachementLigneFixeVM();
        fixeVM.setIdClient(codeClient);
        fixeVM.setLogin(login);
        fixeVM.setNumero(numFixe);
        fixeVM.setTypeNumero(TypeNumero.FIXE);

        rattachementLigneServiceImpl.addRattachementLigneFixe(fixeVM);
    }


    @Test(expected = NotFoundNumberException.class)
    public void testRegisterRattachementLigneFixeOfferNotFound() {
        mockAccountB2CRepository.flush();
        mockRattachementLigneRepository.flush();
        String codeClient = "966666";
        String numFixe = "338237294";
        String login = "770502003";

        // sauvegarde user in Account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero(login);
        accountB2C.setFirstName("hello");
        accountB2C.setLastName("hello");
        AccountB2C saveAccount = mockAccountB2CRepository.save(accountB2C);

        //Mock response api get CustomerOffer
        ResponseEntity responseEntity = ResponseEntity.notFound().build();
        when(customerOfferApiClient.getCustomerOffer(anyString())).thenReturn(responseEntity);

        //call register ligne fixe
        RattachementLigneFixeVM fixeVM = new RattachementLigneFixeVM();
        fixeVM.setIdClient(codeClient);
        fixeVM.setLogin(login);
        fixeVM.setNumero(numFixe);
        fixeVM.setTypeNumero(TypeNumero.FIXE);

        rattachementLigneServiceImpl.addRattachementLigneFixe(fixeVM);
    }


    @Test(expected = BadRequestAlertException.class)
    public void testRegisterRattachementLigneFixeWithOrganizationNumber() {

        String codeClient = "32722731";
        String numFixe = "338237244";
        String login = "770502323";

        // sauvegarde user in Account
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero(login);
        accountB2C.setFirstName("hello");
        accountB2C.setLastName("hello");
        AccountB2C saveAccount = mockAccountB2CRepository.save(accountB2C);

        //Mock response api get CustomerOffer
        CustomerOffer customerOffer = getCustomer();
        customerOffer.setClientCode(codeClient);

        ResponseEntity responseEntity = ResponseEntity.ok(customerOffer);
        when(customerOfferApiClient.getCustomerOffer(anyString())).thenReturn(responseEntity);

        InfoClientWrapper infoClientWrapper = new InfoClientWrapper();
        infoClientWrapper.setClientType(ClientType.ORGANIZATION);
        when(abonneService.getInformations(anyString())).thenReturn(infoClientWrapper);

        //call register ligne fixe
        RattachementLigneFixeVM fixeVM = new RattachementLigneFixeVM();
        fixeVM.setIdClient(customerOffer.getClientCode());
        fixeVM.setLogin(login);
        fixeVM.setNumero(numFixe);
        fixeVM.setTypeNumero(TypeNumero.FIXE);

        rattachementLigneServiceImpl.addRattachementLigneFixe(fixeVM);


    }


    @Test
    public void testGetRattachementLignesWithoutCallingCustomerOffer() {

        // Setup
        String msisdn = "77000 00 00";
        mockAccountB2CRepository = mock(AccountB2CRepository.class);
        mockRattachementLigneRepository = mock(RattachementLigneRepository.class);
        rattachementLigneServiceImpl = new RattachementLigneServiceImpl(mockRattachementLigneRepository, mockAccountB2CRepository, customerOfferApiClient, sponseeService, abonneService, selfcareOTPService);

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setId(45L);
        accountB2C.setLastName("");
        accountB2C.setFirstName("");
        accountB2C.setNumero(msisdn);

        Optional<AccountB2C> b2COptional = Optional.of(accountB2C);
        when(mockAccountB2CRepository.findOneByNumero(anyString())).thenReturn(b2COptional);

        List<RattachementLigne> rattachementLignes = new ArrayList<>();

        RattachementLigne ligne = new RattachementLigne();
        ligne.setAccountB2C(accountB2C);
        ligne.setTypeNumero(TypeNumero.MOBILE);
        ligne.setNumero("782363572");

        rattachementLignes.add(ligne);
        when(mockRattachementLigneRepository.findAllByAccountB2C(any())).thenReturn(rattachementLignes);


        // Run the test
        final List<InfoNumberVM> result = rattachementLigneServiceImpl.getRattachementLignes(msisdn, false);

        List<InfoNumberVM> expectedResult = new ArrayList<>();

        // Verify the results
        assertEquals("782363572", result.get(0).getMsisdn());
        assertEquals("", result.get(0).getProfil());
        assertEquals("", result.get(0).getFormule());
    }

    @Test(expected = BadRequestAlertException.class)
    public void testRattachementLignesByCNIWithBadRequest() throws IOException {

        RattachementLigneCNIVM ligneCNIVM = getRattachementLigneCNIVM();
        AccountB2C account = getAccount();
        account.setNumero(ligneCNIVM.getLogin());
        account.setEmail(ligneCNIVM.getLogin() + "@orange.com");
        mockAccountB2CRepository.save(account);
        InfoClientWrapper infoClientWrapperIndividual = getInfoClientWrapperIndividual();
        when(abonneService.getInformations(anyString())).thenReturn(infoClientWrapperIndividual);

        rattachementLigneServiceImpl.rattachementLigneByCni(ligneCNIVM);
    }

    @Test
    public void testRattachementLignesByCNIIndividual() throws IOException {

        RattachementLigneCNIVM ligneCNIVM = getRattachementLigneCNIVM();
        ligneCNIVM.setNumero("781040956");
        ligneCNIVM.setLogin("780000001");
        ligneCNIVM.setIdentificationId("000000060000");
        AccountB2C account = getAccount();
        account.setNumero(ligneCNIVM.getLogin());
        account.setEmail(ligneCNIVM.getLogin() + "@orange.com");
        mockAccountB2CRepository.save(account);
        InfoClientWrapper infoClientWrapperIndividual = getInfoClientWrapperIndividual();
        when(abonneService.getInformations(anyString())).thenReturn(infoClientWrapperIndividual);

        RattachementLigne rattachementLigne = rattachementLigneServiceImpl.rattachementLigneByCni(ligneCNIVM);

        assertEquals(ligneCNIVM.getLogin(), rattachementLigne.getAccountB2C().getNumero());
        assertEquals(ligneCNIVM.getNumero(), rattachementLigne.getNumero());


    }

    @Test
    public void testRattachementLignesByCNIOrganization() throws IOException {

        RattachementLigneCNIVM ligneCNIVM = getRattachementLigneCNIVM();
        ligneCNIVM.setNumero("780000056");
        ligneCNIVM.setLogin("780000002");
        ligneCNIVM.setIdentificationId("SNDKR1999B1877");
        AccountB2C account = getAccount();
        account.setNumero(ligneCNIVM.getLogin());
        account.setEmail(ligneCNIVM.getLogin() + "@orange.com");
        mockAccountB2CRepository.save(account);
        InfoClientWrapper infoClientWrapperIndividual = getInfoClientWrapperOrganization();
        when(abonneService.getInformations(anyString())).thenReturn(infoClientWrapperIndividual);

        RattachementLigne rattachementLigne = rattachementLigneServiceImpl.rattachementLigneByCni(ligneCNIVM);

        assertEquals(ligneCNIVM.getLogin(), rattachementLigne.getAccountB2C().getNumero());
        assertEquals(ligneCNIVM.getNumero(), rattachementLigne.getNumero());


    }

    private RattachementLigneCNIVM getRattachementLigneCNIVM() {
        RattachementLigneCNIVM ligneCNIVM = new RattachementLigneCNIVM();
        ligneCNIVM.setIdentificationId("CNI11111111111111");
        ligneCNIVM.setLogin("781210941");
        ligneCNIVM.setNumero("770000011");
        ligneCNIVM.setTypeNumero(TypeNumero.MOBILE);
        return ligneCNIVM;
    }

    private AccountB2C getAccount() {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("");
        accountB2C.setId(143L);
        accountB2C.setEmail("");
        accountB2C.setLastName("");
        accountB2C.setFirstName("");
        return accountB2C;
    }

    private InfoClientWrapper getInfoClientWrapperOrganization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InfoClientWrapper infoClientWrapper = mapper.readValue(RESPONSE_NUMERO_ORGANIZATION, InfoClientWrapper.class);
        return infoClientWrapper;
    }

    private InfoClientWrapper getInfoClientWrapperIndividual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InfoClientWrapper infoClientWrapper = mapper.readValue(RESPONSE_NUMERO_INDIVIDUAL, InfoClientWrapper.class);

        return infoClientWrapper;
    }


}
