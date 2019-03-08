package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.search.AccountB2CSearchRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicesCall.IServiceUAA;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountB2CResource REST controller.
 *
 * @see AccountB2CResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SelfcareB2CApp.class})
public class AccountB2CResourceIntTest {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_PRFIL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PRFIL = "BBBBBBBBBB";

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    /**
     * This repository is mocked in the sn.sonatel.dsi.dif.selfcare.b2c.repository.search test package.
     *
     * @see sn.sonatel.dsi.dif.selfcare.b2c.repository.search.AccountB2CSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccountB2CSearchRepository mockAccountB2CSearchRepository;

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

    @Autowired
    private RattachementLigneRepository rattachementLigneRepository;

    @Autowired
    private IServiceUAA iServiceUAA;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccountB2CResource accountB2CResource = new AccountB2CResource(accountB2CRepository, mockAccountB2CSearchRepository, rattachementLigneRepository, iServiceUAA);
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
        AccountB2C accountB2C = new AccountB2C()
            .numero(DEFAULT_NUMERO)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .imagePrfil(DEFAULT_IMAGE_PRFIL);
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

        // Create the AccountB2C
        restAccountB2CMockMvc.perform(post("/api/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountB2C)))
            .andExpect(status().isCreated());

        // Validate the AccountB2C in the database
        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeCreate + 1);
        AccountB2C testAccountB2C = accountB2CList.get(accountB2CList.size() - 1);
        assertThat(testAccountB2C.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testAccountB2C.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAccountB2C.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAccountB2C.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAccountB2C.getImagePrfil()).isEqualTo(DEFAULT_IMAGE_PRFIL);

        // Validate the AccountB2C in Elasticsearch
        verify(mockAccountB2CSearchRepository, times(1)).save(testAccountB2C);
    }

    @Test
    @Transactional
    public void createAccountB2CWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountB2CRepository.findAll().size();

        // Create the AccountB2C with an existing ID
        accountB2C.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountB2CMockMvc.perform(post("/api/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountB2C)))
            .andExpect(status().isBadRequest());

        // Validate the AccountB2C in the database
        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeCreate);

        // Validate the AccountB2C in Elasticsearch
        verify(mockAccountB2CSearchRepository, times(0)).save(accountB2C);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountB2CRepository.findAll().size();
        // set the field null
        accountB2C.setNumero(null);

        // Create the AccountB2C, which fails.

        restAccountB2CMockMvc.perform(post("/api/account-b-2-cs")
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

        restAccountB2CMockMvc.perform(post("/api/account-b-2-cs")
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

        restAccountB2CMockMvc.perform(post("/api/account-b-2-cs")
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
        restAccountB2CMockMvc.perform(get("/api/account-b-2-cs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountB2C.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].imagePrfil").value(hasItem(DEFAULT_IMAGE_PRFIL.toString())));
    }
    
    @Test
    @Transactional
    public void getAccountB2C() throws Exception {
        // Initialize the database
        accountB2CRepository.saveAndFlush(accountB2C);

        // Get the accountB2C
        restAccountB2CMockMvc.perform(get("/api/account-b-2-cs/{id}", accountB2C.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accountB2C.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.imagePrfil").value(DEFAULT_IMAGE_PRFIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountB2C() throws Exception {
        // Get the accountB2C
        restAccountB2CMockMvc.perform(get("/api/account-b-2-cs/{id}", Long.MAX_VALUE))
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
        updatedAccountB2C
            .numero(UPDATED_NUMERO)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .imagePrfil(UPDATED_IMAGE_PRFIL);

        restAccountB2CMockMvc.perform(put("/api/account-b-2-cs")
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
        assertThat(testAccountB2C.getImagePrfil()).isEqualTo(UPDATED_IMAGE_PRFIL);

        // Validate the AccountB2C in Elasticsearch
        verify(mockAccountB2CSearchRepository, times(1)).save(testAccountB2C);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountB2C() throws Exception {
        int databaseSizeBeforeUpdate = accountB2CRepository.findAll().size();

        // Create the AccountB2C

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountB2CMockMvc.perform(put("/api/account-b-2-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accountB2C)))
            .andExpect(status().isBadRequest());

        // Validate the AccountB2C in the database
        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountB2C in Elasticsearch
        verify(mockAccountB2CSearchRepository, times(0)).save(accountB2C);
    }

    @Test
    @Transactional
    public void deleteAccountB2C() throws Exception {
        // Initialize the database
        accountB2CRepository.saveAndFlush(accountB2C);

        int databaseSizeBeforeDelete = accountB2CRepository.findAll().size();

        // Delete the accountB2C
        restAccountB2CMockMvc.perform(delete("/api/account-b-2-cs/{id}", accountB2C.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccountB2C> accountB2CList = accountB2CRepository.findAll();
        assertThat(accountB2CList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AccountB2C in Elasticsearch
        verify(mockAccountB2CSearchRepository, times(1)).deleteById(accountB2C.getId());
    }

    @Test
    @Transactional
    public void searchAccountB2C() throws Exception {
        // Initialize the database
        accountB2CRepository.saveAndFlush(accountB2C);
        when(mockAccountB2CSearchRepository.search(queryStringQuery("id:" + accountB2C.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accountB2C), PageRequest.of(0, 1), 1));
        // Search the accountB2C
        restAccountB2CMockMvc.perform(get("/api/_search/account-b-2-cs?query=id:" + accountB2C.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountB2C.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imagePrfil").value(hasItem(DEFAULT_IMAGE_PRFIL)));
    }

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
}
