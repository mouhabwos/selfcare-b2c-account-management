package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.createFormattingConversionService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsor;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponsorRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponsorService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the LogsResource REST controller.
 *
 * @see SponsorResource
 */
@RunWith(SpringRunner.class)
@IntegrationTest
class SponsorResourceIntTest {

    private static final String DEFAULT_MSISDN = "AAAAAAAAAA";
    private static final String UPDATED_MSISDN = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        final SponsorResource sponsorResource = new SponsorResource(sponsorService);
        this.restSponsorMockMvc =
            MockMvcBuilders
                .standaloneSetup(sponsorResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator)
                .build();
    }

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private SponsorService sponsorService;

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

    private MockMvc restSponsorMockMvc;

    private Sponsor sponsor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    static Sponsor createEntity(EntityManager em) {
        Sponsor sponsor = new Sponsor()
            .msisdn(DEFAULT_MSISDN)
            .matricule(DEFAULT_MATRICULE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME);
        return sponsor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    static Sponsor createUpdatedEntity(EntityManager em) {
        Sponsor sponsor = new Sponsor()
            .msisdn(UPDATED_MSISDN)
            .matricule(UPDATED_MATRICULE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);
        return sponsor;
    }

    @BeforeEach
    void initTest() {
        sponsor = createEntity(em);
    }

    @Test
    @Transactional
    void createSponsor() throws Exception {
        int databaseSizeBeforeCreate = sponsorRepository.findAll().size();

        // Create the Sponsor
        restSponsorMockMvc
            .perform(post("/api/sponsors").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(sponsor)))
            .andExpect(status().isCreated());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeCreate + 1);
        Sponsor testSponsor = sponsorList.get(sponsorList.size() - 1);
        assertThat(testSponsor.getMsisdn()).isEqualTo(DEFAULT_MSISDN);
        assertThat(testSponsor.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testSponsor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSponsor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void createSponsorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sponsorRepository.findAll().size();

        // Create the Sponsor with an existing ID
        sponsor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSponsorMockMvc
            .perform(post("/api/sponsors").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(sponsor)))
            .andExpect(status().isBadRequest());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSponsors() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get all the sponsorList
        restSponsorMockMvc
            .perform(get("/api/sponsors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sponsor.getId().intValue())))
            .andExpect(jsonPath("$.[*].msisdn").value(hasItem(DEFAULT_MSISDN.toString())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())));
    }

    @Test
    @Transactional
    void getSponsor() throws Exception {
        // Initialize the database
        sponsorRepository.saveAndFlush(sponsor);

        // Get the sponsor
        restSponsorMockMvc
            .perform(get("/api/sponsors/{id}", sponsor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sponsor.getId().intValue()))
            .andExpect(jsonPath("$.msisdn").value(DEFAULT_MSISDN.toString()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()));
    }

    @Test
    void getSponsorByMsisdn() throws Exception {
        String msisdn = "776713165";

        // Get the sponsor
        restSponsorMockMvc.perform(get("/api/sponsors/{msisdn}/check", msisdn)).andExpect(status().isOk());
    }

    @Test
    @Transactional
    void getNonExistingSponsor() throws Exception {
        // Get the sponsor
        restSponsorMockMvc.perform(get("/api/sponsors/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateSponsor() throws Exception {
        // Initialize the database
        sponsorService.save(sponsor);

        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();

        // Update the sponsor
        Sponsor updatedSponsor = sponsorRepository.findById(sponsor.getId()).get();
        // Disconnect from session so that the updates on updatedSponsor are not directly saved in db
        em.detach(updatedSponsor);
        updatedSponsor.msisdn(UPDATED_MSISDN).matricule(UPDATED_MATRICULE).firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restSponsorMockMvc
            .perform(
                put("/api/sponsors").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(updatedSponsor))
            )
            .andExpect(status().isOk());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
        Sponsor testSponsor = sponsorList.get(sponsorList.size() - 1);
        assertThat(testSponsor.getMsisdn()).isEqualTo(UPDATED_MSISDN);
        assertThat(testSponsor.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testSponsor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSponsor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void updateNonExistingSponsor() throws Exception {
        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();

        // Create the Sponsor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSponsorMockMvc
            .perform(put("/api/sponsors").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(sponsor)))
            .andExpect(status().isBadRequest());

        // Validate the Sponsor in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void uploadSponsor() throws Exception {
        int databaseSizeBeforeUpdate = sponsorRepository.findAll().size();

        //        Mock Multipart
        Path path = Paths.get("sponsors.xlsx");
        String name = "file";
        String originalFileName = "sponsors.xlsx";
        String contentType = "text/plain";
        byte[] content = Files.readAllBytes(path);
        MockMultipartFile file = new MockMultipartFile(name, originalFileName, contentType, content);

        restSponsorMockMvc.perform(MockMvcRequestBuilders.multipart("/api/sponsors/upload").file(file)).andExpect(status().isAccepted());

        // Validate the sim in the database
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        Assert.assertEquals(databaseSizeBeforeUpdate + 1, sponsorList.size());
    }

    @Test
    @Transactional
    void deleteSponsor() throws Exception {
        // Initialize the database
        sponsorService.save(sponsor);

        int databaseSizeBeforeDelete = sponsorRepository.findAll().size();

        // Delete the sponsor
        restSponsorMockMvc
            .perform(delete("/api/sponsors/{id}", sponsor.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sponsor> sponsorList = sponsorRepository.findAll();
        assertThat(sponsorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sponsor.class);
        Sponsor sponsor1 = new Sponsor();
        sponsor1.setId(1L);
        Sponsor sponsor2 = new Sponsor();
        sponsor2.setId(sponsor1.getId());
        assertThat(sponsor1).isEqualTo(sponsor2);
        sponsor2.setId(2L);
        assertThat(sponsor1).isNotEqualTo(sponsor2);
        sponsor1.setId(null);
        assertThat(sponsor1).isNotEqualTo(sponsor2);
    }
}
