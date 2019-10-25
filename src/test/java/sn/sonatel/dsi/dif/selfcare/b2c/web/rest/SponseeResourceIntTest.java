package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponseeService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.mapper.SponseeMapper;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.sameInstant;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SponseeResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SelfcareB2CApp.class})
public class SponseeResourceIntTest {

    private static final String DEFAULT_MSISDN = "AAAAAAAAAA";
    private static final String UPDATED_MSISDN = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EFFECTIVE = false;
    private static final Boolean UPDATED_EFFECTIVE = true;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private SponseeRepository sponseeRepository;

    @Autowired
    private SponseeMapper sponseeMapper;

    @Autowired
    private SponseeService sponseeService;

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

    private MockMvc restSponseeMockMvc;

    private Sponsee sponsee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SponseeResource sponseeResource = new SponseeResource(sponseeService);
        this.restSponseeMockMvc = MockMvcBuilders.standaloneSetup(sponseeResource)
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
    public static Sponsee createEntity(EntityManager em) {
        Sponsee sponsee = new Sponsee()
            .msisdn(DEFAULT_MSISDN)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .effective(DEFAULT_EFFECTIVE)
            .createdDate(DEFAULT_CREATED_DATE)
            .enabled(DEFAULT_ENABLED);
        return sponsee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sponsee createUpdatedEntity(EntityManager em) {
        Sponsee sponsee = new Sponsee()
            .msisdn(UPDATED_MSISDN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .effective(UPDATED_EFFECTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .enabled(UPDATED_ENABLED);
        return sponsee;
    }

    @Before
    public void initTest() {
        sponsee = createEntity(em);
    }

    @Test
    @Transactional
    public void createSponsee() throws Exception {
        int databaseSizeBeforeCreate = sponseeRepository.findAll().size();

        // Create the Sponsee
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);
        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isCreated());

        // Validate the Sponsee in the database
        List<Sponsee> sponseeList = sponseeRepository.findAll();
        assertThat(sponseeList).hasSize(databaseSizeBeforeCreate + 1);
        Sponsee testSponsee = sponseeList.get(sponseeList.size() - 1);
        assertThat(testSponsee.getMsisdn()).isEqualTo(DEFAULT_MSISDN);
        assertThat(testSponsee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSponsee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testSponsee.isEffective()).isEqualTo(DEFAULT_EFFECTIVE);
        assertThat(testSponsee.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSponsee.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createSponseeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sponseeRepository.findAll().size();

        // Create the Sponsee with an existing ID
        sponsee.setId(1L);
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sponsee in the database
        List<Sponsee> sponseeList = sponseeRepository.findAll();
        assertThat(sponseeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkMsisdnIsRequired() throws Exception {
        int databaseSizeBeforeTest = sponseeRepository.findAll().size();
        // set the field null
        sponsee.setMsisdn(null);

        // Create the Sponsee, which fails.
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);

        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isBadRequest());

        List<Sponsee> sponseeList = sponseeRepository.findAll();
        assertThat(sponseeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEffectiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = sponseeRepository.findAll().size();
        // set the field null
        sponsee.setEffective(null);

        // Create the Sponsee, which fails.
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);

        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isCreated());

        List<Sponsee> sponseeList = sponseeRepository.findAll();
        int result = databaseSizeBeforeTest + 1;
        assertThat(sponseeList).hasSize(result);
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = sponseeRepository.findAll().size();
        // set the field null
        sponsee.setCreatedDate(null);

        // Create the Sponsee, which fails.
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);

        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isCreated());

        List<Sponsee> sponseeList = sponseeRepository.findAll();
        int result = databaseSizeBeforeTest + 1;
        assertThat(sponseeList).hasSize(result);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = sponseeRepository.findAll().size();
        // set the field null
        sponsee.setEnabled(null);

        // Create the Sponsee, which fails.
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);

        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isCreated());

        List<Sponsee> sponseeList = sponseeRepository.findAll();
        int result = databaseSizeBeforeTest + 1;
        assertThat(sponseeList).hasSize(result);
    }

    @Test
    @Transactional
    public void getAllSponsees() throws Exception {
        // Initialize the database
        sponseeRepository.saveAndFlush(sponsee);

        // Get all the sponseeList
        restSponseeMockMvc.perform(get("/api/sponsees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sponsee.getId().intValue())))
            .andExpect(jsonPath("$.[*].msisdn").value(hasItem(DEFAULT_MSISDN.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].effective").value(hasItem(DEFAULT_EFFECTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getSponsee() throws Exception {
        // Initialize the database
        sponseeRepository.saveAndFlush(sponsee);

        // Get the sponsee
        restSponseeMockMvc.perform(get("/api/sponsees/{id}", sponsee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sponsee.getId().intValue()))
            .andExpect(jsonPath("$.msisdn").value(DEFAULT_MSISDN.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.effective").value(DEFAULT_EFFECTIVE.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSponsee() throws Exception {
        // Get the sponsee
        restSponseeMockMvc.perform(get("/api/sponsees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSponsee() throws Exception {
        // Initialize the database
        sponseeRepository.saveAndFlush(sponsee);

        int databaseSizeBeforeUpdate = sponseeRepository.findAll().size();

        // Update the sponsee
        Sponsee updatedSponsee = sponseeRepository.findById(sponsee.getId()).get();
        // Disconnect from session so that the updates on updatedSponsee are not directly saved in db
        em.detach(updatedSponsee);
        updatedSponsee
            .msisdn(UPDATED_MSISDN)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .effective(UPDATED_EFFECTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .enabled(UPDATED_ENABLED);
        SponseeDTO sponseeDTO = sponseeMapper.toDto(updatedSponsee);

        restSponseeMockMvc.perform(put("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isOk());

        // Validate the Sponsee in the database
        List<Sponsee> sponseeList = sponseeRepository.findAll();
        assertThat(sponseeList).hasSize(databaseSizeBeforeUpdate);
        Sponsee testSponsee = sponseeList.get(sponseeList.size() - 1);
        assertThat(testSponsee.getMsisdn()).isEqualTo(UPDATED_MSISDN);
        assertThat(testSponsee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSponsee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSponsee.isEffective()).isEqualTo(UPDATED_EFFECTIVE);
        assertThat(testSponsee.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSponsee.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingSponsee() throws Exception {
        int databaseSizeBeforeUpdate = sponseeRepository.findAll().size();

        // Create the Sponsee
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSponseeMockMvc.perform(put("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sponsee in the database
        List<Sponsee> sponseeList = sponseeRepository.findAll();
        assertThat(sponseeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSponsee() throws Exception {
        // Initialize the database
        sponseeRepository.saveAndFlush(sponsee);

        int databaseSizeBeforeDelete = sponseeRepository.findAll().size();

        // Delete the sponsee
        restSponseeMockMvc.perform(delete("/api/sponsees/{id}", sponsee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sponsee> sponseeList = sponseeRepository.findAll();
        assertThat(sponseeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sponsee.class);
        Sponsee sponsee1 = new Sponsee();
        sponsee1.setId(1L);
        Sponsee sponsee2 = new Sponsee();
        sponsee2.setId(sponsee1.getId());
        assertThat(sponsee1).isEqualTo(sponsee2);
        sponsee2.setId(2L);
        assertThat(sponsee1).isNotEqualTo(sponsee2);
        sponsee1.setId(null);
        assertThat(sponsee1).isNotEqualTo(sponsee2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {

        SponseeDTO sponseeDTO1 = new SponseeDTO();
        sponseeDTO1.setId(1L);
        SponseeDTO sponseeDTO2 = new SponseeDTO();
        assertThat(sponseeDTO1).isNotEqualTo(sponseeDTO2);
        sponseeDTO2.setId(sponseeDTO1.getId());

        sponseeDTO2.setId(2L);
        assertThat(sponseeDTO1).isNotEqualTo(sponseeDTO2);
        sponseeDTO1.setId(null);
        assertThat(sponseeDTO1).isNotEqualTo(sponseeDTO2);
        sponseeDTO2 = sponseeDTO1;
        assertThat(sponseeDTO1).isEqualTo(sponseeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sponseeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sponseeMapper.fromId(null)).isNull();
    }


    @Test
    @Transactional
    public void testSendSmsForSponseeWithBadRequest() throws Exception {

        String msisdnSource = "770000000";
        String msisdnDest = "770000001";

        // Create the Sponsee, which fails.
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);

        restSponseeMockMvc.perform(post("/api/sponsees/send-sms?msisdnSource="+msisdnSource+"&msisdnDest="+msisdnDest)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isBadRequest());

    }

}
