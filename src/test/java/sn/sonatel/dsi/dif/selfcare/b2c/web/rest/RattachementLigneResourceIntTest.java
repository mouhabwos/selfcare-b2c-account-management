package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;


import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.RattachementLigneRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.RattachementLigneService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RattachementLigneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareSoapService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.Assert.assertTrue;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLigneVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.RattachementLignesDeleteMultipleVM;

/**
 * Test class for the RattachementLigneResource REST controller.
 *
 * @see RattachementLigneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SelfcareB2CApp.class})
public class RattachementLigneResourceIntTest {

    private static final String DEFAULT_NUMERO = "778505050";
    private static final String UPDATED_NUMERO = "778505052";

    private static final String DEFAULT_TYPE_VERIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_VERIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_VERIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_VERIFICATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUT = false;
    private static final Boolean UPDATED_STATUT = true;


    private static final TypeNumero DEFAULT_TYPE_NUMERO = TypeNumero.FIX;
    private static final TypeNumero UPDATED_TYPE_NUMERO = TypeNumero.MOBILE;

    @Autowired
    private RattachementLigneRepository rattachementLigneRepository;


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

    private MockMvc restRattachementLigneMockMvc;

    private RattachementLigne rattachementLigne;

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate restTemplate;
    @Mock
    private SelfcareSoapService selfcareSoapService;

    private RattachementLigneResource rattachementLigneResource;

    @Autowired
    private RattachementLigneService rattachementLigneService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        rattachementLigneResource = new RattachementLigneResource(rattachementLigneService);
        this.restRattachementLigneMockMvc = MockMvcBuilders.standaloneSetup(rattachementLigneResource)
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
    public static RattachementLigne createEntity(EntityManager em) {
        RattachementLigne rattachementLigne = new RattachementLigne()
            .typeNumero(DEFAULT_TYPE_NUMERO);
        rattachementLigne.setNumero(UPDATED_NUMERO);
        return rattachementLigne;
    }

    @Before
    public void initTest() {
        rattachementLigne = createEntity(em);
    }

    @Test
    @Transactional
    public void createRattachementLigne() throws Exception {
        int databaseSizeBeforeCreate = rattachementLigneRepository.findAll().size();

        RattachementLigneDTO ligne = new RattachementLigneDTO();
        ligne.setTypeNumero(rattachementLigne.getTypeNumero());
        ligne.setAccountB2C(rattachementLigne.getAccountB2C());
        ligne.setNumero(rattachementLigne.getNumero());



        // Create the RattachementLigne
        restRattachementLigneMockMvc.perform(post("/api/rattachement-lignes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligne)))
            .andExpect(status().isCreated());

        // Validate the RattachementLigne in the database
        List<RattachementLigne> rattachementLigneList = rattachementLigneRepository.findAll();
        assertThat(rattachementLigneList).hasSize(databaseSizeBeforeCreate + 1);
        RattachementLigne testRattachementLigne = rattachementLigneList.get(rattachementLigneList.size() - 1);
        assertThat(testRattachementLigne.getNumero()).isEqualTo("778505052");
        assertThat(testRattachementLigne.getTypeNumero()).isEqualTo(DEFAULT_TYPE_NUMERO);

    }

    @Test
    @Transactional
    public void createRattachementLigneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rattachementLigneRepository.findAll().size();

        // Create the RattachementLigne with an existing ID
        rattachementLigne.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRattachementLigneMockMvc.perform(post("/api/rattachement-lignes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rattachementLigne)))
            .andExpect(status().isBadRequest());

        // Validate the RattachementLigne in the database
        List<RattachementLigne> rattachementLigneList = rattachementLigneRepository.findAll();
        assertThat(rattachementLigneList).hasSize(databaseSizeBeforeCreate);

    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = rattachementLigneRepository.findAll().size();
        // set the field null
        rattachementLigne.setNumero(null);

        // Create the RattachementLigne, which fails.

        restRattachementLigneMockMvc.perform(post("/api/rattachement-lignes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rattachementLigne)))
            .andExpect(status().isBadRequest());

        List<RattachementLigne> rattachementLigneList = rattachementLigneRepository.findAll();
        assertThat(rattachementLigneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = rattachementLigneRepository.findAll().size();
        // set the field null
        rattachementLigne.setTypeNumero(null);

        // Create the RattachementLigne, which fails.

        restRattachementLigneMockMvc.perform(post("/api/rattachement-lignes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rattachementLigne)))
            .andExpect(status().isBadRequest());

        List<RattachementLigne> rattachementLigneList = rattachementLigneRepository.findAll();
        assertThat(rattachementLigneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRattachementLignes() throws Exception {
        // Initialize the database
        rattachementLigneRepository.saveAndFlush(rattachementLigne);

        // Get all the rattachementLigneList
        restRattachementLigneMockMvc.perform(get("/api/rattachement-lignes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rattachementLigne.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem("778505052")))
            .andExpect(jsonPath("$.[*].typeNumero").value(hasItem(DEFAULT_TYPE_NUMERO.toString())));
    }


    @Test
    @Transactional
    public void getAllRattachementLignesByMsisdn() throws Exception {
        // Initialize the database
        rattachementLigneRepository.saveAndFlush(rattachementLigne);
        addRattachement();

        // Get all the rattachementLigneList
        restRattachementLigneMockMvc.perform(get("/api/rattachement-lignes/get-all-number/{msisdn}", "775167600"))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getRattachementLigne() throws Exception {
        // Initialize the database
        rattachementLigneRepository.saveAndFlush(rattachementLigne);

        // Get the rattachementLigne
        restRattachementLigneMockMvc.perform(get("/api/rattachement-lignes/{id}", rattachementLigne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rattachementLigne.getId().intValue()))
            .andExpect(jsonPath("$.numero").value("778505052"))
            .andExpect(jsonPath("$.typeNumero").value(DEFAULT_TYPE_NUMERO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRattachementLigne() throws Exception {
        // Get the rattachementLigne
        restRattachementLigneMockMvc.perform(get("/api/rattachement-lignes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRattachementLigne() throws Exception {
        // Initialize the database
        rattachementLigneRepository.saveAndFlush(rattachementLigne);

        int databaseSizeBeforeUpdate = rattachementLigneRepository.findAll().size();

        // Update the rattachementLigne
        RattachementLigne updatedRattachementLigne = rattachementLigneRepository.findById(rattachementLigne.getId()).get();
        // Disconnect from session so that the updates on updatedRattachementLigne are not directly saved in db
        em.detach(updatedRattachementLigne);
        updatedRattachementLigne
            .typeNumero(UPDATED_TYPE_NUMERO);
        updatedRattachementLigne.setNumero(UPDATED_NUMERO);

        restRattachementLigneMockMvc.perform(put("/api/rattachement-lignes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRattachementLigne)))
            .andExpect(status().isOk());

        // Validate the RattachementLigne in the database
        List<RattachementLigne> rattachementLigneList = rattachementLigneRepository.findAll();
        assertThat(rattachementLigneList).hasSize(databaseSizeBeforeUpdate);
        RattachementLigne testRattachementLigne = rattachementLigneList.get(rattachementLigneList.size() - 1);
        assertThat(testRattachementLigne.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testRattachementLigne.getTypeNumero()).isEqualTo(UPDATED_TYPE_NUMERO);


    }

    @Test
    @Transactional
    public void updateNonExistingRattachementLigne() throws Exception {
        int databaseSizeBeforeUpdate = rattachementLigneRepository.findAll().size();

        // Create the RattachementLigne

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRattachementLigneMockMvc.perform(put("/api/rattachement-lignes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rattachementLigne)))
            .andExpect(status().isBadRequest());

        // Validate the RattachementLigne in the database
        List<RattachementLigne> rattachementLigneList = rattachementLigneRepository.findAll();
        assertThat(rattachementLigneList).hasSize(databaseSizeBeforeUpdate);

    }

    @Test
    @Transactional
    public void deleteRattachementLigne() throws Exception {
        // Initialize the database
        rattachementLigneRepository.saveAndFlush(rattachementLigne);

        int databaseSizeBeforeDelete = rattachementLigneRepository.findAll().size();

        // Delete the rattachementLigne
        restRattachementLigneMockMvc.perform(delete("/api/rattachement-lignes/{id}", rattachementLigne.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RattachementLigne> rattachementLigneList = rattachementLigneRepository.findAll();
        assertThat(rattachementLigneList).hasSize(databaseSizeBeforeDelete - 1);

    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RattachementLigne.class);
        RattachementLigne rattachementLigne1 = new RattachementLigne();
        rattachementLigne1.setId(1L);
        RattachementLigne rattachementLigne2 = new RattachementLigne();
        rattachementLigne2.setId(rattachementLigne1.getId());
        assertThat(rattachementLigne1).isEqualTo(rattachementLigne2);
        rattachementLigne2.setId(2L);
        assertThat(rattachementLigne1).isNotEqualTo(rattachementLigne2);
        rattachementLigne1.setId(null);
        assertThat(rattachementLigne1).isNotEqualTo(rattachementLigne2);
    }

    public void addRattachement() {
        AccountB2C u = new AccountB2C();
        u.setNumero("775167600");
        u.setFirstName("leyla");
        u.setLastName("diallo");
        u.setEmail("diall@gmail.com");

        accountB2CRepository.save(u);

        AccountB2C u1 = new AccountB2C();
        u1.setNumero("775167602");
        u1.setFirstName("leyla");
        u1.setLastName("diallo");
        u1.setEmail("dial@gmail.com");
        accountB2CRepository.save(u1);

        RattachementLigne ligne1 = new RattachementLigne();
        ligne1.setNumero("772502592");
        ligne1.setAccountB2C(u);
        ligne1.setTypeNumero(UPDATED_TYPE_NUMERO);
        rattachementLigneRepository.save(ligne1);

    }

    @Test
    public void deleteMultipleRattachementLigne() throws Exception {

        addRattachement();
        // Initialize the database
        rattachementLigneRepository.save(rattachementLigne);

        List<String> numberToDelete = new ArrayList<>();
        numberToDelete.add("772502592");
        RattachementLignesDeleteMultipleVM deleteVM = new RattachementLignesDeleteMultipleVM();

        deleteVM.setListMsisdn(numberToDelete);
        deleteVM.setLogin(DEFAULT_NUMERO);

        restRattachementLigneMockMvc
            .perform(post("/api/rattachement-lignes/delete-multiple")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deleteVM)))
            .andExpect(status().isOk());

        Optional<RattachementLigne> opLign1 = rattachementLigneRepository
            .findByNumero("772502592");
        assertThat(opLign1).isEmpty();
    }

    @Test
    @Transactional
    public void getRattachementLignesByMsisdn() throws Exception {

        // Initialize the database
        rattachementLigneRepository.saveAndFlush(rattachementLigne);

        // Get the rattachementLigne
        restRattachementLigneMockMvc.perform(get("/api/rattachement-lignes/get-all-number/{msisdn}", "775167600"))
            .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void addRattachementLigne() throws Exception {

        AccountB2C u = new AccountB2C();
        u.setNumero("775167605");
        u.setFirstName("leyla");
        u.setLastName("diallo");
        u.setEmail("dia@gmail.com");

        accountB2CRepository.save(u);
        int databaseSizeBeforeCreate = rattachementLigneRepository.findAll().size();

        RattachementLigneVM ligneVM = new RattachementLigneVM();

        ligneVM.setLogin(u.getNumero());
        ligneVM.setNumero("771326617");
        ligneVM.setTypeNumero(UPDATED_TYPE_NUMERO);

        // Create the RattachementLigne
        restRattachementLigneMockMvc.perform(post("/api/rattachement-lignes/register")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneVM)))
            .andExpect(status().isCreated());

        // Validate the RattachementLigne in the database
        List<RattachementLigne> rattachementLigneList = rattachementLigneRepository.findAll();
        assertThat(rattachementLigneList).hasSize(databaseSizeBeforeCreate + 1);
        RattachementLigne testRattachementLigne = rattachementLigneList.get(rattachementLigneList.size() - 1);
        assertThat(testRattachementLigne.getNumero()).isEqualTo("771326617");
        assertThat(testRattachementLigne.getTypeNumero()).isEqualTo(UPDATED_TYPE_NUMERO);

    }

    @Test
    @Transactional
    public void addRattachementLigneLoginNotFound() throws Exception {

        int databaseSizeBeforeCreate = rattachementLigneRepository.findAll().size();

        RattachementLigneVM ligneVM = new RattachementLigneVM();

        ligneVM.setLogin("770236606");
        ligneVM.setNumero("771326617");
        ligneVM.setTypeNumero(UPDATED_TYPE_NUMERO);


        // Create the RattachementLigne
        restRattachementLigneMockMvc.perform(post("/api/rattachement-lignes/register")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneVM)))
            .andExpect(status().isBadRequest());


    }

    @Test
    @Transactional
    public void addRattachementLigneExistingLigne() throws Exception {

        AccountB2C u = new AccountB2C();
        u.setNumero("775167605");
        u.setFirstName("leyla");
        u.setLastName("diallo");
        u.setEmail("dia@gmail.com");

        accountB2CRepository.save(u);
        int databaseSizeBeforeCreate = rattachementLigneRepository.findAll().size();

        RattachementLigneVM ligneVM = new RattachementLigneVM();

        ligneVM.setLogin(u.getNumero());
        ligneVM.setNumero("771326617");
        ligneVM.setTypeNumero(UPDATED_TYPE_NUMERO);
         rattachementLigne.setTypeNumero(UPDATED_TYPE_NUMERO);
         rattachementLigne.setNumero("771326617");
         rattachementLigne.setAccountB2C(u);
        rattachementLigneRepository.save(rattachementLigne);

        // Create the RattachementLigne
        restRattachementLigneMockMvc.perform(post("/api/rattachement-lignes/register")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneVM)))
            .andExpect(status().isBadRequest());


    }

    @Test
    @Transactional
    public void addRattachementLigneExistingUser() throws Exception {

        AccountB2C u = new AccountB2C();
        u.setNumero("775167605");
        u.setFirstName("leyla");
        u.setLastName("diallo");
        u.setEmail("dia@gmail.com");

        accountB2CRepository.save(u);
        int databaseSizeBeforeCreate = rattachementLigneRepository.findAll().size();

        RattachementLigneVM ligneVM = new RattachementLigneVM();

        ligneVM.setLogin(u.getNumero());
        ligneVM.setNumero("771326617");
        ligneVM.setTypeNumero(UPDATED_TYPE_NUMERO);

        // Create the RattachementLigne
        restRattachementLigneMockMvc.perform(post("/api/rattachement-lignes/register")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ligneVM)))
            .andExpect(status().isCreated());


    }

  /*  @Test
    public void getSouscription() {

        SouscriptionDto dto = new SouscriptionDto();
        dto.setNomOffre("Diamano Allo");
        dto.setProfil("PREPAID");

      //  when(rattachementLigneResource.getSouscription(Mockito.any())).thenReturn(dto);

        SouscriptionDto entity = rattachementLigneResource.getSouscription("774505050");

        //assertTrue(entity.equals(dto));
    }
*/


}
