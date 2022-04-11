package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

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
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.DeviceInfos;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.DeviceInfosRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.createFormattingConversionService;

/**
 * Integration tests for the {@link DeviceInfosResource} REST controller.
 */
@RunWith(SpringRunner.class)
@IntegrationTest
public class DeviceInfosResourceIntTest {

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    @Autowired
    private DeviceInfosRepository deviceInfosRepository;

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

    private MockMvc restDeviceInfosMockMvc;

    private DeviceInfos deviceInfos;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeviceInfosResource deviceInfosResource = new DeviceInfosResource(deviceInfosRepository);
        this.restDeviceInfosMockMvc = MockMvcBuilders.standaloneSetup(deviceInfosResource)
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
    public static DeviceInfos createEntity(EntityManager em) {
        DeviceInfos deviceInfos = new DeviceInfos()
            .deviceId(DEFAULT_DEVICE_ID);
        return deviceInfos;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceInfos createUpdatedEntity(EntityManager em) {
        DeviceInfos deviceInfos = new DeviceInfos()
            .deviceId(UPDATED_DEVICE_ID);
        return deviceInfos;
    }

    @Before
    public void initTest() {
        deviceInfos = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeviceInfos() throws Exception {
        int databaseSizeBeforeCreate = deviceInfosRepository.findAll().size();

        // Create the DeviceInfos
        restDeviceInfosMockMvc.perform(post("/api/device-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceInfos)))
            .andExpect(status().isCreated());

        // Validate the DeviceInfos in the database
        List<DeviceInfos> deviceInfosList = deviceInfosRepository.findAll();
        assertThat(deviceInfosList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceInfos testDeviceInfos = deviceInfosList.get(deviceInfosList.size() - 1);
        assertThat(testDeviceInfos.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
    }

    @Test
    @Transactional
    public void createDeviceInfosWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deviceInfosRepository.findAll().size();

        // Create the DeviceInfos with an existing ID
        deviceInfos.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceInfosMockMvc.perform(post("/api/device-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceInfos)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceInfos in the database
        List<DeviceInfos> deviceInfosList = deviceInfosRepository.findAll();
        assertThat(deviceInfosList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDeviceInfos() throws Exception {
        // Initialize the database
        deviceInfosRepository.saveAndFlush(deviceInfos);

        // Get all the deviceInfosList
        restDeviceInfosMockMvc.perform(get("/api/device-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceInfos.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID.toString())));
    }

    @Test
    @Transactional
    public void getDeviceInfos() throws Exception {
        // Initialize the database
        deviceInfosRepository.saveAndFlush(deviceInfos);

        // Get the deviceInfos
        restDeviceInfosMockMvc.perform(get("/api/device-infos/{id}", deviceInfos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(deviceInfos.getId().intValue()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeviceInfos() throws Exception {
        // Get the deviceInfos
        restDeviceInfosMockMvc.perform(get("/api/device-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeviceInfos() throws Exception {
        // Initialize the database
        deviceInfosRepository.saveAndFlush(deviceInfos);

        int databaseSizeBeforeUpdate = deviceInfosRepository.findAll().size();

        // Update the deviceInfos
        DeviceInfos updatedDeviceInfos = deviceInfosRepository.findById(deviceInfos.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceInfos are not directly saved in db
        em.detach(updatedDeviceInfos);
        updatedDeviceInfos
            .deviceId(UPDATED_DEVICE_ID);

        restDeviceInfosMockMvc.perform(put("/api/device-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeviceInfos)))
            .andExpect(status().isOk());

        // Validate the DeviceInfos in the database
        List<DeviceInfos> deviceInfosList = deviceInfosRepository.findAll();
        assertThat(deviceInfosList).hasSize(databaseSizeBeforeUpdate);
        DeviceInfos testDeviceInfos = deviceInfosList.get(deviceInfosList.size() - 1);
        assertThat(testDeviceInfos.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingDeviceInfos() throws Exception {
        int databaseSizeBeforeUpdate = deviceInfosRepository.findAll().size();

        // Create the DeviceInfos

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceInfosMockMvc.perform(put("/api/device-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceInfos)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceInfos in the database
        List<DeviceInfos> deviceInfosList = deviceInfosRepository.findAll();
        assertThat(deviceInfosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDeviceInfos() throws Exception {
        // Initialize the database
        deviceInfosRepository.saveAndFlush(deviceInfos);

        int databaseSizeBeforeDelete = deviceInfosRepository.findAll().size();

        // Delete the deviceInfos
        restDeviceInfosMockMvc.perform(delete("/api/device-infos/{id}", deviceInfos.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceInfos> deviceInfosList = deviceInfosRepository.findAll();
        assertThat(deviceInfosList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceInfos.class);
        DeviceInfos deviceInfos1 = new DeviceInfos();
        deviceInfos1.setId(1L);
        DeviceInfos deviceInfos2 = new DeviceInfos();
        deviceInfos2.setId(deviceInfos1.getId());
        assertThat(deviceInfos1).isEqualTo(deviceInfos2);
        deviceInfos2.setId(2L);
        assertThat(deviceInfos1).isNotEqualTo(deviceInfos2);
        deviceInfos1.setId(null);
        assertThat(deviceInfos1).isNotEqualTo(deviceInfos2);
    }
}
