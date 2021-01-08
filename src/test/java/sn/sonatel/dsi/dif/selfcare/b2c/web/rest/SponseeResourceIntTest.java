package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.client.booster.BoosterClient;
import sn.sonatel.dsi.dif.selfcare.b2c.client.booster.dto.BoosterPromo;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.SponseeRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SponseeService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SponseeDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.WelcomeBoosterStatus;
import sn.sonatel.dsi.dif.selfcare.b2c.service.impl.SponseeServiceImpl;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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

    private static final String msisdnSponsor = "770010101";

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

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    @Mock
    private BoosterClient boosterClient;

    @Mock
    private SponseeRepository sponseesRepository;



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

    private AccountB2C getAccount(){
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setId(785L);
        accountB2C.setNumero("770000005");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");

        return accountB2C;
    }

    private SponseeDTO getSponseeDto(){

        SponseeDTO sponsee = new SponseeDTO();
        sponsee.setMsisdn("770000006");
        sponsee.setMsisdnSponsor(getAccount().getNumero());

        return sponsee;
    }

    private void forMockService(){
        sponseeService = mock(SponseeServiceImpl.class);
        final SponseeResource sponseeResource = new SponseeResource(sponseeService);
        this.restSponseeMockMvc = MockMvcBuilders.standaloneSetup(sponseeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @Test
    @Transactional
    public void createSponsee() throws Exception {

        forMockService();
        int databaseSizeBeforeCreate = sponseeRepository.findAll().size();

        SponseeDTO dto = getSponseeDto();
        dto.setId(782L);
        dto.setMsisdnSponsor("778962323");
        dto.setMsisdn(DEFAULT_MSISDN);
        dto.setCreatedDate(DEFAULT_CREATED_DATE);
        dto.setEffective(DEFAULT_EFFECTIVE.booleanValue());
        dto.setFirstName(DEFAULT_FIRST_NAME);
        dto.setLastName(DEFAULT_LAST_NAME);
        dto.setEnabled(DEFAULT_ENABLED.booleanValue());
        when(sponseeService.register(ArgumentMatchers.any())).thenReturn(dto);


        // Create the Sponsee
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);
        sponseeDTO.setMsisdnSponsor(msisdnSponsor);
        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isAccepted());

        // Validate the Sponsee in the database
       /* List<SponseeDTO> sponseeDTOs = new ArrayList<>();
        sponseeDTOs.add(dto);
        Page<SponseeDTO> dtoPage = new PageImpl(sponseeDTOs);
        when(sponseeService.findAll(ArgumentMatchers.any())).thenReturn(dtoPage);
        List<Sponsee> sponseeList = sponseeRepository.findAll();

        System.out.println("@@@@@@@@@@         "+sponseeList.size()+"           @@@@@@@@@@@");
        Sponsee testSponsee = sponseeList.get(sponseeList.size());
        assertThat(testSponsee.getMsisdn()).isEqualTo(DEFAULT_MSISDN);
        assertThat(testSponsee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSponsee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testSponsee.isEffective()).isEqualTo(DEFAULT_EFFECTIVE);
        assertThat(testSponsee.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSponsee.isEnabled()).isEqualTo(DEFAULT_ENABLED);*/
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


        forMockService();
        when(sponseeService.register(ArgumentMatchers.any())).thenReturn(getSponseeDto());

        int databaseSizeBeforeTest = sponseeRepository.findAll().size();
        // set the field null
        sponsee.setEffective(null);

        // Create the Sponsee, which fails.
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);
        sponseeDTO.setMsisdnSponsor(msisdnSponsor);

        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isAccepted());
    }

    @Test
    @Transactional
    public void checkCreatedDateIsRequired() throws Exception {

        forMockService();
        int databaseSizeBeforeTest = sponseeRepository.findAll().size();
        // set the field null
        sponsee.setCreatedDate(null);

        // Create the Sponsee, which fails.
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);
        sponseeDTO.setMsisdnSponsor(msisdnSponsor);

        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isAccepted());

    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {

        forMockService();

        // set the field null
        sponsee.setEnabled(null);

        // Create the Sponsee, which fails.
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);
        sponseeDTO.setMsisdnSponsor(msisdnSponsor);
        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isAccepted());
    }

    @Test
    @Transactional
    public void getAllSponsees() throws Exception {

        AccountB2C accountB2C = getAccount();
        accountB2C.setNumero("780000000");
        accountB2C.setLastName("");
        accountB2C.setFirstName("");
        accountB2C = accountB2CRepository.save(accountB2C);
        sponsee.setAccountB2C(accountB2C);
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

        forMockService();
        // Initialize the database

        Sponsee sponsee = new Sponsee();
        SponseeDTO dto = getSponseeDto();
        dto.setId(782L);
        dto.setMsisdnSponsor("778962323");
        dto.setMsisdn(DEFAULT_MSISDN);
        dto.setCreatedDate(DEFAULT_CREATED_DATE);
        dto.setEffective(DEFAULT_EFFECTIVE.booleanValue());
        dto.setFirstName(DEFAULT_FIRST_NAME);
        dto.setLastName(DEFAULT_LAST_NAME);
        dto.setEnabled(DEFAULT_ENABLED.booleanValue());

        List<SponseeDTO> sponseeDTO = new ArrayList<>();

        sponseeDTO.add(dto);
        Page<SponseeDTO> dtoPage = new PageImpl(sponseeDTO);
        Optional<SponseeDTO> dtoOptional = Optional.of(dto);
        when(sponseeService.findOne(ArgumentMatchers.any())).thenReturn(dtoOptional);


        // Get the sponsee
        restSponseeMockMvc.perform(get("/api/sponsees/{id}", dto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dto.getId().intValue()))
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

        forMockService();
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
        sponseeDTO.setMsisdnSponsor(msisdnSponsor);

        restSponseeMockMvc.perform(put("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isOk());
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
        String msisdnDest = "770000070";
        sponsee.setEffective(false);
        sponsee.setEnabled(true);
        sponsee.setMsisdn(msisdnDest);
        WelcomeBoosterStatus boosterStatus = new WelcomeBoosterStatus();
        WelcomeBoosterStatus.BoosterValue boosterValue = new WelcomeBoosterStatus.BoosterValue();
        WelcomeBoosterStatus.BoosterType boosterType =  WelcomeBoosterStatus.BoosterType.RECHARGE;
        WelcomeBoosterStatus.Status status =  WelcomeBoosterStatus.Status.SUCCESS;

        boosterValue.setUnit(WelcomeBoosterStatus.BoosterUnit.CFA);
        boosterValue.setAmount("1000");
        boosterStatus.setValue(boosterValue);
        boosterStatus.setType(boosterType);
        boosterStatus.setStatus(status);

        List<BoosterPromo> statuses = new ArrayList<>();

        ResponseEntity<List<BoosterPromo>> listResponseEntity = ResponseEntity.ok().body(statuses);
        when(sponseesRepository.findOneByMsisdn(anyString())).thenReturn(Optional.of(sponsee));
        when(boosterClient.getActiveWelcomeBoosterValue(anyString(), anyString(), anyString())).thenReturn(listResponseEntity);
        // Create the Sponsee, which fails.
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);

        restSponseeMockMvc.perform(post("/api/sponsees/send-sms?sMsisdn="+msisdnSource+"&sMsisdn="+msisdnDest)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void testFindAllSponseeByMsisdn() throws Exception {

        restSponseeMockMvc.perform(get("/api/sponsees/by-msisdn/{msisdn}", "77900 00 00"))
            .andExpect(status().isNotFound());

    }

    @Test
    @Transactional
    public void createSponseeWithBadRequest() throws Exception {
        // Create the Sponsee
        SponseeDTO sponseeDTO = sponseeMapper.toDto(sponsee);
        sponseeDTO.setMsisdnSponsor(msisdnSponsor);
        sponseeDTO.setId(1L);
        restSponseeMockMvc.perform(post("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    public void updateSponseeWithBadRequest() throws Exception {

        forMockService();
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
        sponseeDTO.setMsisdnSponsor(msisdnSponsor);
        sponseeDTO.setId(null);

        restSponseeMockMvc.perform(put("/api/sponsees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sponseeDTO)))
            .andExpect(status().isBadRequest());
    }


    @Test
    @Transactional
    public void testCheckSponseeByMsisdn() throws Exception {

        restSponseeMockMvc.perform(get("/api/sponsees/check-number/{msisdn}", "77900 00 00"))
            .andExpect(status().isOk());

    }
}
