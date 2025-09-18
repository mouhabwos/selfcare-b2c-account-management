package sn.sonatel.dsi.dif.selfcare.b2c.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.sonatel.dsi.dif.selfcare.b2c.web.rest.TestUtil.createFormattingConversionService;

import java.io.File;
import java.nio.file.Files;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.FileInformation;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.SearchFilterItem;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.FileInformationRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.FileInformationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.export.ExportService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.ExceptionTranslator;

@RunWith(SpringRunner.class)
@IntegrationTest
class ExportResourceIntTest {

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

    @Autowired
    private ExportService exportService;

    @Autowired
    private AccountB2CRepository accountB2CRepository;

    @Autowired
    private FileInformationService fileInformationService;

    @Autowired
    private FileInformationRepository informationRepository;

    private MockMvc restMockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        final ExportResource exportResource = new ExportResource(exportService, fileInformationService);
        this.restMockMvc =
            MockMvcBuilders
                .standaloneSetup(exportResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator)
                .build();
    }

    private List<FileInformation> getFileInformations() {
        List<FileInformation> fileInformationList = new ArrayList<>();
        FileInformation file1 = new FileInformation();
        FileInformation file2 = new FileInformation();
        FileInformation file3 = new FileInformation();

        file1.setFileName("Information_Account_1617287722028.csv");
        file1.setStatus("FAILED");
        file1.setCreatedByUser("system");
        file1.setCreatedDate(ZonedDateTime.parse("2021-04-01T14:36:13Z"));
        fileInformationList.add(file1);

        file2.setFileName("Information_Account_1617287722027.csv");
        file2.setStatus("FAILED");
        file2.setCreatedByUser("system");
        file2.setCreatedDate(ZonedDateTime.parse("2021-04-01T20:36:01Z"));
        fileInformationList.add(file2);

        file3.setFileName("Information_Account_1617287722026.csv");
        file3.setStatus("COMPLED");
        file3.setCreatedByUser("user");
        file3.setCreatedDate(ZonedDateTime.parse("2021-04-02T09:39:33Z"));
        fileInformationList.add(file3);

        return fileInformationList;
    }

    @Test
    @Transactional
    void testExportAllUsers() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("export@orange-sonatel.com");

        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("770000000");
        accountB2C.setFirstName("");
        accountB2C.setLastName("");

        accountB2CRepository.save(accountB2C);

        AccountB2C account2 = new AccountB2C();
        account2.setNumero("770000001");
        account2.setFirstName("");
        account2.setLastName("");

        accountB2CRepository.save(account2);

        restMockMvc.perform(post("/api/export/all-users").principal(mockPrincipal)).andExpect(status().isAccepted());
    }

    @Test
    void testImporFileMsisdn() throws Exception {
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("export@orange-sonatel.com");

        File resourcesDirectory = new File("src/test/resources/files/numero.csv");

        MockMultipartFile file = new MockMultipartFile("file", "files/numero.csv", "", Files.readAllBytes(resourcesDirectory.toPath()));

        restMockMvc
            .perform(multipart("/api/export/v1/file-campaign-flow").file(file).principal(mockPrincipal))
            .andExpect(status().isAccepted());
    }

    @Test
    void testInformationFileUploadedByUserAndDates() throws Exception {
        List<FileInformation> fileInformations = getFileInformations();
        informationRepository.saveAll(fileInformations);
        restMockMvc
            .perform(
                get("/api/export/v1/uploaded-file-information")
                    .param("searchFilterItem", SearchFilterItem.BETWEEN_TWO_DATES.name())
                    .param("user", "system")
                    .param("startDate", "2021-04-01T14:36:13Z")
                    .param("endDate", "2021-04-02T09:39:33Z")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem("2021-04-01T20:36:01Z")));
    }

    @Test
    void testInformationFileUploadedByDateWithBadRequestResponse() throws Exception {
        restMockMvc
            .perform(get("/api/export/v1/uploaded-file-information").param("searchFilterItem", SearchFilterItem.BETWEEN_TWO_DATES.name()))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testGetListInformationFileUploaded() throws Exception {
        List<FileInformation> fileInformations = getFileInformations();
        informationRepository.saveAll(fileInformations);

        restMockMvc
            .perform(get("/api/export/v1/uploaded-file-information").param("searchFilterItem", SearchFilterItem.ALL.name()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem("Information_Account_1617287722027.csv")))
            .andExpect(jsonPath("$.[*].createdByUser").value(hasItem("system")))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem("2021-04-01T20:36:01Z")))
            .andExpect(jsonPath("$.[*].status").value(hasItem("FAILED")));
    }

    @Test
    void testGetListInformationFileUploadedByUser() throws Exception {
        List<FileInformation> fileInformations = getFileInformations();
        informationRepository.saveAll(fileInformations);
        informationRepository.flush();
        restMockMvc
            .perform(
                get("/api/export/v1/uploaded-file-information")
                    .param("searchFilterItem", SearchFilterItem.BY_USER.name())
                    .param("user", "system")
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem("Information_Account_1617287722028.csv")))
            .andExpect(jsonPath("$.[*].createdByUser").value(hasItem("system")))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem("2021-04-01T14:36:13Z")))
            .andExpect(jsonPath("$.[*].status").value(hasItem("FAILED")));
    }

    @Test
    void testInformationFileUploadedByUserWithBadRequestResponse() throws Exception {
        restMockMvc
            .perform(get("/api/export/v1/uploaded-file-information").param("searchFilterItem", SearchFilterItem.BY_USER.name()))
            .andExpect(status().isBadRequest());
    }
}
