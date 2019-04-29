package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.DowloadManager;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AccountB2CDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.EmailExistDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserInfoOuvertureCompte;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.createFormattingConversionService;

/**
 * Test class for the AccountB2CResource REST controller.
 *
 * @see AccountB2CResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelfcareB2CApp.class})
public class AccountB2CResourceIntTest {

    private static final String DEFAULT_NUMERO = "778505050";
    private static final String UPDATED_NUMERO = "778505055";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAA@gmail.com";
    private static final String UPDATED_EMAIL = "BBBBB@gmail.com";

    private static final String DEFAULT_IMAGE_PRFIL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PRFIL = "BBBBBBBBBB";

    private static final TypeNumero TYPE_NUMERO_MOBILE = TypeNumero.MOBILE;

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAccountB2CMockMvc;


    private AccountB2C accountB2C;

    @Mock
    private AccountB2CResource accountB2CResource;

    @Autowired
    private RattachementLigneRepository rattachementLigneRepository;

    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private MailService mailService;

    @Mock
    private DowloadManager dowloadManager;

    @Autowired
    private AccountB2CResource accountBCResource;

    @Autowired
    private AccountB2CService accountB2CService;



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccountB2CResource accountB2CResource = new AccountB2CResource(accountB2CRepository, rattachementLigneRepository, restTemplate, mailService, dowloadManager, accountB2CService);
        this.restAccountB2CMockMvc = MockMvcBuilders.standaloneSetup(accountB2CResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountB2C createEntity(EntityManager em) {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero(DEFAULT_NUMERO);
        accountB2C.setFirstName(DEFAULT_FIRST_NAME);
        accountB2C.setLastName(DEFAULT_LAST_NAME);
        accountB2C.setEmail(DEFAULT_EMAIL);
        accountB2C.setImageProfil(DEFAULT_IMAGE_PRFIL);
        return accountB2C;
    }

    @Before
    public void initTest() {
        accountB2C = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccountB2C() throws Exception {
        int databaseSizeBeforeCreate = accountB2CRepository.findAll().size();

        AccountB2CDTO b2C = new AccountB2CDTO();
        b2C.setNumero(accountB2C.getNumero());
        b2C.setEmail(accountB2C.getEmail());
        b2C.setFirstName(accountB2C.getFirstName());
        b2C.setLastName(accountB2C.getLastName());
        b2C.setImageProfil(accountB2C.getImageProfil());

        // Create the AccountB2C
        restAccountB2CMockMvc.perform(post("/api/account-management/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(b2C)))
            .andExpect(status().isCreated());

        // Validate the AccountB2C in the database
        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeCreate + 1);
        AccountB2C testAccountB2C = accountB2CList.get(accountB2CList.size() - 1);
        assertThat(testAccountB2C.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testAccountB2C.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAccountB2C.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAccountB2C.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAccountB2C.getImageProfil()).isEqualTo(DEFAULT_IMAGE_PRFIL);

    }

    @Test
    @Transactional
    public void createAccountB2CMock() throws Exception {
        int databaseSizeBeforeCreate = accountB2CRepository.findAll().size();

        AccountB2CDTO b2C = new AccountB2CDTO();
        b2C.setNumero(accountB2C.getNumero());
        b2C.setEmail(accountB2C.getEmail());
        b2C.setFirstName(accountB2C.getFirstName());
        b2C.setLastName(accountB2C.getLastName());
        b2C.setImageProfil(accountB2C.getImageProfil());
        ResponseEntity<AccountB2C>  response = ResponseEntity.status(HttpStatus.CREATED).build();
        when(accountB2CResource.registerAccountB2C(Mockito.any())).thenReturn(response);
        // Create the AccountB2C
        restAccountB2CMockMvc.perform(post("/api/account-management/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(b2C)))
            .andExpect(status().isCreated());


    }


    @Test
    @Transactional
    public void createAccountB2CWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountB2CRepository.findAll().size();

        // Create the AccountB2C with an existing ID
        accountB2C.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountB2CMockMvc.perform(post("/api/account-management/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountB2C)))
            .andExpect(status().isBadRequest());

        // Validate the AccountB2C in the database
        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeCreate);

    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountB2CRepository.findAll().size();
        // set the field null
        accountB2C.setNumero(null);

        // Create the AccountB2C, which fails.

        restAccountB2CMockMvc.perform(post("/api/account-management/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountB2C)))
            .andExpect(status().isBadRequest());

        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountB2CRepository.findAll().size();
        // set the field null
        accountB2C.setFirstName(null);

        // Create the AccountB2C, which fails.

        restAccountB2CMockMvc.perform(post("/api/account-management/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountB2C)))
            .andExpect(status().isBadRequest());

        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountB2CRepository.findAll().size();
        // set the field null
        accountB2C.setLastName(null);

        // Create the AccountB2C, which fails.

        restAccountB2CMockMvc.perform(post("/api/account-management/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountB2C)))
            .andExpect(status().isBadRequest());

        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccountB2CS() throws Exception {
        // Initialize the database
        accountB2CRepository.saveAndFlush(accountB2C);

        // Get all the accountB2CList
        restAccountB2CMockMvc.perform(get("/api/account-management/account-b-2-cs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountB2C.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imageProfil").value(hasItem(DEFAULT_IMAGE_PRFIL)));
    }

/*    @Test
    @Transactional
    public void getAccountB2C() throws Exception {
        // Initialize the database
        accountB2CRepository.saveAndFlush(accountB2C);

        // Get the accountB2C
        restAccountB2CMockMvc.perform(get("/api/account-management/account-b-2-cs/{id}", accountB2C.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountB2C.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.imageProfil").value(DEFAULT_IMAGE_PRFIL.toString()));
    }*/

    @Test
    @Transactional
    public void getNonExistingAccountB2C() throws Exception {
        // Get the accountB2C
        restAccountB2CMockMvc.perform(get("/api/account-management/account-b-2-cs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountB2C() throws Exception {
        // Initialize the database
        accountB2CRepository.saveAndFlush(accountB2C);

        int databaseSizeBeforeUpdate = accountB2CRepository.findAll().size();

        // Update the accountB2C
        AccountB2C updatedAccountB2C = accountB2CRepository.findById(accountB2C.getId()).get();
        // Disconnect from session so that the updates on updatedAccountB2C are not directly saved in db
        em.detach(updatedAccountB2C);

        updatedAccountB2C.setLastName(UPDATED_LAST_NAME);
        updatedAccountB2C.setNumero(UPDATED_NUMERO);
        updatedAccountB2C.setFirstName(UPDATED_FIRST_NAME);
        updatedAccountB2C.setLastName(UPDATED_LAST_NAME);
        updatedAccountB2C.setEmail(UPDATED_EMAIL);
        updatedAccountB2C.setImageProfil(UPDATED_IMAGE_PRFIL);

        restAccountB2CMockMvc.perform(put("/api/account-management/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccountB2C)))
            .andExpect(status().isOk());

        // Validate the AccountB2C in the database
        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeUpdate);
        AccountB2C testAccountB2C = accountB2CList.get(accountB2CList.size() - 1);
        assertThat(testAccountB2C.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testAccountB2C.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAccountB2C.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAccountB2C.getEmail()).isEqualTo(UPDATED_EMAIL);

    }

    @Test
    @Transactional
    public void updateNonExistingAccountB2C() throws Exception {
        int databaseSizeBeforeUpdate = accountB2CRepository.findAll().size();

        // Create the AccountB2C

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountB2CMockMvc.perform(put("/api/account-management/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountB2C)))
            .andExpect(status().isBadRequest());

        // Validate the AccountB2C in the database
        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeUpdate);

    }

 /*   @Test
    @Transactional
    public void deleteAccountB2C() throws Exception {
        // Initialize the database
        accountB2CRepository.saveAndFlush(accountB2C);

        int databaseSizeBeforeDelete = accountB2CRepository.findAll().size();

        // Delete the accountB2C
        restAccountB2CMockMvc.perform(delete("/api/account-management/account-b-2-cs/{id}", accountB2C.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeDelete - 1);

    }*/

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountB2C.class);
        AccountB2C accountB2C1 = new AccountB2C();
        accountB2C1.setId(1L);
        AccountB2C accountB2C2 = new AccountB2C();
        accountB2C2.setId(accountB2C1.getId());
        assertThat(accountB2C1).isEqualTo(accountB2C2);
        accountB2C2.setId(2L);
        assertThat(accountB2C1).isNotEqualTo(accountB2C2);
        accountB2C1.setId(null);
        assertThat(accountB2C1).isNotEqualTo(accountB2C2);
    }

    @Test
    @Transactional
    public void checkLoginUsed() throws Exception {
        // Initialize the database
        accountB2CRepository.saveAndFlush(accountB2C);

        // Get the accountB2C
        restAccountB2CMockMvc.perform(get("/api/account-management/check_number/{msisdn}", accountB2C.getNumero()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void checkLoginAlreadyRattached() throws Exception {
        // Initialize the database
        accountB2CRepository.saveAndFlush(accountB2C);

        RattachementLigne ligne = new RattachementLigne();
        ligne.setNumero("771222323");
        ligne.setAccountB2C(accountB2C);
        ligne.setTypeNumero(TYPE_NUMERO_MOBILE);
        rattachementLigneRepository.save(ligne);
        // Get the accountB2C
        restAccountB2CMockMvc.perform(get("/api/account-management/check_number/{msisdn}", ligne.getNumero()))
            .andExpect(status().isBadRequest());
    }


    @Test
    @Transactional
    public void checkLoginNotUsed() throws Exception {

        // Get the accountB2C
        restAccountB2CMockMvc.perform(get("/api/account-management/check_number/{msisdn}","779853232"))
            .andExpect(status().isOk());
    }

    public void addData(){
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setEmail("test@gmail.com");
        accountB2C.setFirstName("test1");
        accountB2C.setLastName("test2");
        accountB2C.setNumero("778522323");

        accountB2CRepository.save(accountB2C);
    }

    @Test
    @Transactional
    public void getAccountWithNumberValid() throws Exception{
        addData();
        String login = "778522323";
        restAccountB2CMockMvc.perform(get("/api/account-management/account/{msisdn}",login ))
            .andExpect(status().isOk());

    }


    @Test
    @Transactional
    public void getAccountWithLoginInvalid() throws Exception{
        addData();
        String login = "value";
        restAccountB2CMockMvc.perform(get("/api/account-management/account/{msisdn}",login ))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void getAccountWithNumberNotFound() throws Exception{
        addData();
        String login = "779606060";
        restAccountB2CMockMvc.perform(get("/api/account-management/account/{msisdn}",login ))
            .andExpect(status().isBadRequest());
    }




    public ManagedUserVM addDataManagedUserVM(){

        ManagedUserVM vm = new ManagedUserVM();

        vm.setEmail("value@gmail.com");
        vm.setLastName("lastname");
        vm.setFirstName("firsname");
        vm.setLogin("771234545");
        vm.setPassword("Passer12");
        vm.setActivated(true);
        vm.setActivationKey("fr");
        vm.setCreatedDate(null);
        vm.setLangKey("fr");

        return vm;
    }

    public void adRattachement(){

        RattachementLigne ligne = new RattachementLigne();
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setEmail("test@gmail.com");
        accountB2C.setFirstName("test1");
        accountB2C.setLastName("test2");
        accountB2C.setNumero("778522323");

        accountB2C = accountB2CRepository.save(accountB2C);
        ligne.setAccountB2C(accountB2C);
        ligne.setNumero("770256363");
        ligne.setTypeNumero(TYPE_NUMERO_MOBILE);
        ligne.setTypeVerification("test");
        ligne.setCodeVerification("test");
        ligne.setStatut(true);
        rattachementLigneRepository.save(ligne);

    }

    @Test
    @Transactional
    public void registerAccountB2C() throws Exception {

        int databaseSizeBeforeCreate = accountB2CRepository.findAll().size();
        ManagedUserVM b2C = new ManagedUserVM();

        b2C.setLogin(accountB2C.getNumero());
        b2C.setFirstName(accountB2C.getFirstName());
        b2C.setLastName(accountB2C.getLastName());
        b2C.setPassword("Passer12");
        b2C.setEmail("email4@gmail.com");
        b2C.setLangKey("");
        b2C.setActivationKey("");

        AccountB2C account =  new AccountB2C();
        account.setNumero(b2C.getLogin());
        account.setId(1L);
        account.setFirstName(b2C.getFirstName());
        account.setLastName(b2C.getLastName());
        account.setImageProfil(b2C.getImageprofil());
       // ResponseEntity<AccountB2C>  response = ResponseEntity.status(HttpStatus.CREATED).body(account);
       // when(accountB2CResource.registerAccountB2C(Mockito.any())).thenReturn(response);
        // Create the AccountB2C
/*
       restAccountB2CMockMvc.perform(post("/api/account-management/register")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(b2C)))
            .andExpect(status().isCreated());*/


    }

    @Test
    @Transactional
    public void registerAccountB2CWithNumberRattached() throws Exception {
        adRattachement();
        ManagedUserVM vm =addDataManagedUserVM();
        vm.setLogin("770256363");

        restAccountB2CMockMvc.perform(post("/api/account-management/register")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vm)))
            .andExpect(status().isBadRequest());

    }



    @Transactional
    @Test
    public void emailExistingVerify() throws Exception {
        addData();
        EmailExistDTO existDTO = new EmailExistDTO();
         existDTO.setEmail("test@gmail.com");
        restAccountB2CMockMvc.perform(post("/api/account-management/email-already-exist" )
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existDTO)))
            .andExpect(status().isOk());

    }


    @Test
    public void testSendMail() throws Exception {

        UserInfoOuvertureCompte user = new UserInfoOuvertureCompte();
        user.setNumero("771326617");
        user.setFirstName("bouya");
        user.setLastName("kande");
        user.setOperation("Ouverture compte OM");
        user.setOperationTitle("Ouverture compte OM");
        user.setFormulaire("1.PNG");
        user.setRectoID("1.PNG");
        user.setVersoID("1.PNG");
        user.setEmail("bouya@gmail.com");
      //  accountBCResource.sendmail(user);

        // Mockito.doNothing().when(accountBCResource).sendmail(any(UserInfoOuvertureCompte.class));
        restAccountB2CMockMvc.perform(post("/api/account-management/mail/ouverture-compte" )
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isOk());


    }

    @Test
    public void testSendMailWithEmptyAttachment() throws Exception {

        UserInfoOuvertureCompte user = new UserInfoOuvertureCompte();
        user.setNumero("771326617");
        user.setFirstName("bouya");
        user.setLastName("kande");
        user.setOperation("Ouverture compte OM");
        user.setOperationTitle("Ouverture compte OM");
        user.setFormulaire("1.PNG");
        user.setRectoID("1.PNG");
        user.setEmail("bouya@gmail.com");
        //user.setVersoID("1.PNG");
        //  accountBCResource.sendmail(user);

        // Mockito.doNothing().when(accountBCResource).sendmail(any(UserInfoOuvertureCompte.class));
        restAccountB2CMockMvc.perform(post("/api/account-management/mail/ouverture-compte" )
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isOk());


    }


    /**
     * test for end point :: /view-tutorial/{msisdn}
     */

    @Test
    public void testTutorialView() throws Exception {

        AccountB2C b2C = new AccountB2C();
        b2C.setTutoViewed(false);
        b2C.setNumero("770502595");
        b2C.setLastName("hello");
        b2C.setFirstName("hello");
        b2C.setEmail("hello95@gmail.com");
        b2C = accountB2CRepository.save(b2C);
        restAccountB2CMockMvc.perform(get("/api/account-management/view-tutorial/{msisdn}", b2C.getNumero()))
            .andExpect(status().isOk());
    }

    @Test
    public void testTutorialViewBadRequest() throws Exception {

        restAccountB2CMockMvc.perform(get("/api/account-management/view-tutorial/{msisdn}", "770565053"))
            .andExpect(status().isBadRequest());
    }
}
