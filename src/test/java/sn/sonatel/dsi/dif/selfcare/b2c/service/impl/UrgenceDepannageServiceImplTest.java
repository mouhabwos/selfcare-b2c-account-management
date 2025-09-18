package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.StatusMail;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.MailSendRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.MailService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SFTPClientService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.UrgenceDepannageService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.ValidationHmacService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OperationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

@RunWith(SpringRunner.class)
@IntegrationTest
class UrgenceDepannageServiceImplTest {

    private static final String stringOperationDTO =
        "{\n" +
        "\t\"operationCode\":\"operation-100\",\n" +
        "\"numero\":\"771326617\",\n" +
        "\"lastName\":\"kande\",\n" +
        "\"firstName\":\"bouya\",\n" +
        "\"email\":\"bouyakandee@gmail.com\"\n" +
        "}";
    private static final String stringOperationDTOErrorCode =
        "{\n" +
        "\t\"operationCode\":\"operation-test\",\n" +
        "\"numero\":\"771326617\",\n" +
        "\"lastName\":\"kande\",\n" +
        "\"firstName\":\"bouya\",\n" +
        "\"email\":\"bouyakandee@gmail.com\"\n" +
        "}";
    private static final String CANAL = "";
    private static final String stringOperationDTONoValideMsisdn =
        "{\n" +
        "\t\"operationCode\":\"operation-100\",\n" +
        "\"numero\":\"000046563\",\n" +
        "\"lastName\":\"kande\",\n" +
        "\"firstName\":\"bouya\",\n" +
        "\"email\":\"bouyakandee@gmail.com\"\n" +
        "}";
    private static final String stringOperationDTOWithFixeNumber =
        "{\n" +
        "\t\"operationCode\":\"operation-100\",\n" +
        "\"numero\":\"339962218\",\n" +
        "\"lastName\":\"kande\",\n" +
        "\"firstName\":\"bouya\",\n" +
        "\"email\":\"bouyakandee@gmail.com\"\n" +
        "}";

    private UrgenceDepannageService urgenceDepannageService;

    @Mock
    private MailSendRepository mailSendRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Mock
    private SFTPClientService ftpService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        urgenceDepannageService = new UrgenceDepannageServiceImpl(applicationProperties, ftpService, mailService);
    }

    private OperationDTO operationDTO() throws Exception {
        OperationDTO operationDTO = new OperationDTO();

        String filePdf = "fichierTest.pdf";
        String image = "imageTest.png";

        File filePdfM = new File(filePdf);
        File fileImageRectoM = new File(image);
        File fileImageVersoM = new File(image);

        FileInputStream inputPDF = new FileInputStream(filePdfM);
        FileInputStream inputRecto = new FileInputStream(filePdfM);
        FileInputStream inputVerso = new FileInputStream(filePdfM);

        MultipartFile multipartFilePDF = new MockMultipartFile("file", filePdfM.getName(), "text/plain", IOUtils.toByteArray(inputPDF));
        MultipartFile multipartFileRecto = new MockMultipartFile(
            "file",
            fileImageRectoM.getName(),
            "text/plain",
            IOUtils.toByteArray(inputRecto)
        );
        MultipartFile multipartFileVerso = new MockMultipartFile(
            "file",
            fileImageVersoM.getName(),
            "text/plain",
            IOUtils.toByteArray(inputVerso)
        );

        operationDTO.setFirstName("firstName");
        operationDTO.setOperationCode("operation-200");
        operationDTO.setLastName("lastname");
        operationDTO.setNumero("777777700");
        operationDTO.setEmail("email@gmail.com");
        operationDTO.setFormulaire(multipartFilePDF);
        operationDTO.setRectoID(multipartFileRecto);
        operationDTO.setVerso(multipartFileVerso);

        return operationDTO;
    }

    private OperationDTO operationDTOWithCaracter() throws Exception {
        OperationDTO operationDTO = new OperationDTO();

        String filePdf = "fichierTest.pdf";
        String image = "---éééé ]]@..@@.png";

        File filePdfM = new File(filePdf);
        File fileImageRectoM = new File(image);
        File fileImageVersoM = new File(image);

        FileInputStream inputPDF = new FileInputStream(filePdfM);
        FileInputStream inputRecto = new FileInputStream(filePdfM);
        FileInputStream inputVerso = new FileInputStream(filePdfM);

        MultipartFile multipartFilePDF = new MockMultipartFile("file", filePdfM.getName(), "text/plain", IOUtils.toByteArray(inputPDF));
        MultipartFile multipartFileRecto = new MockMultipartFile(
            "file",
            fileImageRectoM.getName(),
            "text/plain",
            IOUtils.toByteArray(inputRecto)
        );
        MultipartFile multipartFileVerso = new MockMultipartFile(
            "file",
            fileImageVersoM.getName(),
            "text/plain",
            IOUtils.toByteArray(inputVerso)
        );

        operationDTO.setFirstName("firstName");
        operationDTO.setOperationCode("operation-200");
        operationDTO.setLastName("lastname");
        operationDTO.setNumero("777777700");
        operationDTO.setEmail("email@gmail.com");
        operationDTO.setFormulaire(multipartFilePDF);
        operationDTO.setRectoID(multipartFileRecto);
        operationDTO.setVerso(multipartFileVerso);

        return operationDTO;
    }

    private OperationDTO operationDTOErrorRecto() throws Exception {
        OperationDTO operationDTO = new OperationDTO();

        String filePdf = "fichierTest.pdf";
        String image = "imageTest.pngff";

        File filePdfM = new File(filePdf);
        File fileImageRectoM = new File(image);
        File fileImageVersoM = new File(image);

        FileInputStream inputPDF = new FileInputStream(filePdfM);
        FileInputStream inputRecto = new FileInputStream(filePdfM);
        FileInputStream inputVerso = new FileInputStream(filePdfM);

        MultipartFile multipartFilePDF = new MockMultipartFile("file", filePdfM.getName(), "text/plain", IOUtils.toByteArray(inputPDF));
        MultipartFile multipartFileRecto = new MockMultipartFile(
            "file",
            fileImageRectoM.getName(),
            "text/plain",
            IOUtils.toByteArray(inputRecto)
        );
        MultipartFile multipartFileVerso = new MockMultipartFile(
            "file",
            fileImageVersoM.getName(),
            "text/plain",
            IOUtils.toByteArray(inputVerso)
        );

        operationDTO.setFirstName("firstName");
        operationDTO.setOperationCode("operation-200");
        operationDTO.setLastName("lastname");
        operationDTO.setNumero("777777700");
        operationDTO.setEmail("email@gmail.com");
        operationDTO.setFormulaire(multipartFilePDF);
        operationDTO.setRectoID(multipartFileRecto);
        operationDTO.setVerso(multipartFileVerso);

        return operationDTO;
    }

    private Mail getMailEntity() {
        Mail mail = new Mail();

        mail.setIdRequest("mail_465465");
        mail.setId(8L);
        mail.setStatus(StatusMail.IN_PROGRESS);
        mail.setEmail("mail@gmail.com");
        mail.setIdFormulaire("idform");
        mail.setIdVerso("idVerso");
        mail.setIdRecto("idRecto");
        mail.setOperationTitre("title");
        mail.setFirsName("FirsName");
        mail.setLastName("LastName");
        mail.setNumero("777895656");
        return mail;
    }

    @Test
    void testOuvertureCompte() throws Exception {
        // Setup

        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);

        // Run the test
        String ouvertureCompte = urgenceDepannageService.ouvertureCompte(
            stringOperationDTO,
            operationDTO().getFormulaire(),
            operationDTO().getRectoID(),
            operationDTO().getVerso(),
            CANAL
        );
        assertNotEquals("", ouvertureCompte);
    }

    @Test
    void testOuvertureCompteWithCaract() throws Exception {
        // Setup

        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);

        // Run the test
        String ouvertureCompte = urgenceDepannageService.ouvertureCompte(
            stringOperationDTO,
            operationDTOWithCaracter().getFormulaire(),
            operationDTOWithCaracter().getRectoID(),
            operationDTOWithCaracter().getVerso(),
            CANAL
        );
        assertNotEquals("", ouvertureCompte);
    }

    //
    @Test
    void testOuvertureCompteThrowsBadRequestAlertException() throws Exception {
        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);

        // Run the test
        OperationDTO dto = operationDTO();
        dto.setOperationCode("operation-test");

        MultipartFile formulaire = dto.getFormulaire();
        MultipartFile rectoID = dto.getRectoID();
        MultipartFile verso = dto.getVerso();
        BadRequestAlertException badRequestAlertException = Assertions.assertThrows(
            BadRequestAlertException.class,
            () -> urgenceDepannageService.ouvertureCompte(stringOperationDTOErrorCode, formulaire, rectoID, verso, CANAL)
        );

        Assertions.assertEquals("Le code de l operation est introuvable", badRequestAlertException.getTitle());
    }

    @Test
    void testOuvertureCompteNotValideImage() throws Exception {
        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);

        MultipartFile formulaire = operationDTOErrorRecto().getFormulaire();
        MultipartFile rectoID = operationDTOErrorRecto().getRectoID();
        MultipartFile verso = operationDTOErrorRecto().getVerso();

        BadRequestAlertException thrown = Assertions.assertThrows(
            BadRequestAlertException.class,
            () -> {
                urgenceDepannageService.ouvertureCompte(stringOperationDTO, formulaire, rectoID, verso, CANAL);
            },
            "BadRequestAlertException was expected"
        );

        Assertions.assertEquals(
            "Le ficher doit etre soit un document (.pdf, .doc, .docx) ou une image (.png, .jpg, .jpeg)",
            thrown.getTitle()
        );
    }

    @Test
    void testOuvertureCompteNotValideMsisdn() throws Exception {
        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);
        MultipartFile formulaire = operationDTOErrorRecto().getFormulaire();
        MultipartFile rectoID = operationDTOErrorRecto().getRectoID();
        MultipartFile verso = operationDTOErrorRecto().getVerso();

        BadRequestAlertException thrown = Assertions.assertThrows(
            BadRequestAlertException.class,
            () -> {
                urgenceDepannageService.ouvertureCompte(stringOperationDTONoValideMsisdn, formulaire, rectoID, verso, CANAL);
            },
            "BadRequestAlertException was expected"
        );

        Assertions.assertEquals("Ce numéro doit être un numéro orange valide", thrown.getTitle());
    }

    @Test
    void testOuvertureCompteWithFixeNumber() throws Exception {
        // Setup

        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);

        // Run the test

        MultipartFile formulaire = operationDTO().getFormulaire();
        MultipartFile rectoID = operationDTO().getRectoID();
        MultipartFile verso = operationDTO().getVerso();

        BadRequestAlertException thrown = Assertions.assertThrows(
            BadRequestAlertException.class,
            () -> {
                urgenceDepannageService.ouvertureCompte(stringOperationDTOWithFixeNumber, formulaire, rectoID, verso, CANAL);
            },
            "BadRequestAlertException was expected"
        );

        Assertions.assertEquals("Le numero doit etre un numéro mobile orange valide", thrown.getTitle());
    }
}
