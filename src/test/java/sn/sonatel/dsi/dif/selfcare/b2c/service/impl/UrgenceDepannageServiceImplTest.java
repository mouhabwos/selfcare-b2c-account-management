package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.StatusMail;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.MailSendRepository;

import sn.sonatel.dsi.dif.selfcare.b2c.service.MailService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SFTPClientService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OperationDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.UrgenceDepannageService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelfcareB2CApp.class})
public class UrgenceDepannageServiceImplTest {

    private static final String stringOperationDTO = "{\n" + "\t\"operationCode\":\"operation-100\",\n" + "\"numero\":\"771326617\",\n" + "\"lastName\":\"kande\",\n" + "\"firstName\":\"bouya\",\n" + "\"email\":\"bouyakandee@gmail.com\"\n" + "}";
    private static final String stringOperationDTOErrorCode = "{\n" + "\t\"operationCode\":\"operation-test\",\n" + "\"numero\":\"771326617\",\n" + "\"lastName\":\"kande\",\n" + "\"firstName\":\"bouya\",\n" + "\"email\":\"bouyakandee@gmail.com\"\n" + "}";
    private static final String CANAL = "";
    private static final String stringOperationDTONoValideMsisdn = "{\n" + "\t\"operationCode\":\"operation-100\",\n" + "\"numero\":\"000046563\",\n" + "\"lastName\":\"kande\",\n" + "\"firstName\":\"bouya\",\n" + "\"email\":\"bouyakandee@gmail.com\"\n" + "}";

    private UrgenceDepannageService urgenceDepannageService;

    @Mock
    private MailSendRepository mailSendRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Mock
    private SFTPClientService ftpService;

    @Before
    public void setUp() {
        initMocks(this);
        urgenceDepannageService = new UrgenceDepannageServiceImpl(mailSendRepository, applicationProperties, ftpService, mailService);
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
        MultipartFile multipartFileRecto = new MockMultipartFile("file", fileImageRectoM.getName(), "text/plain", IOUtils.toByteArray(inputRecto));
        MultipartFile multipartFileVerso = new MockMultipartFile("file", fileImageVersoM.getName(), "text/plain", IOUtils.toByteArray(inputVerso));


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
        MultipartFile multipartFileRecto = new MockMultipartFile("file", fileImageRectoM.getName(), "text/plain", IOUtils.toByteArray(inputRecto));
        MultipartFile multipartFileVerso = new MockMultipartFile("file", fileImageVersoM.getName(), "text/plain", IOUtils.toByteArray(inputVerso));


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
        MultipartFile multipartFileRecto = new MockMultipartFile("file", fileImageRectoM.getName(), "text/plain", IOUtils.toByteArray(inputRecto));
        MultipartFile multipartFileVerso = new MockMultipartFile("file", fileImageVersoM.getName(), "text/plain", IOUtils.toByteArray(inputVerso));


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

    private Mail getMailEntity(){
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
    public void testOuvertureCompte() throws Exception {

        // Setup

        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);

        // Run the test
        String ouvertureCompte = urgenceDepannageService.ouvertureCompte(stringOperationDTO, operationDTO().getFormulaire(), operationDTO().getRectoID(), operationDTO().getVerso(),CANAL);
        assertEquals( mail.getIdRequest(),ouvertureCompte);
    }

    @Test
    public void testOuvertureCompteWithCaract() throws Exception {

        // Setup

        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);

        // Run the test
        String ouvertureCompte = urgenceDepannageService.ouvertureCompte(stringOperationDTO, operationDTOWithCaracter().getFormulaire(), operationDTOWithCaracter().getRectoID(), operationDTOWithCaracter().getVerso(),CANAL);
        assertEquals( mail.getIdRequest(),ouvertureCompte);
    }

    //
    @Test(expected = BadRequestAlertException.class)
    public void testOuvertureCompteThrowsBadRequestAlertException() throws Exception {

        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);

        // Run the test
        OperationDTO dto = operationDTO();
        dto.setOperationCode("operation-test");
        urgenceDepannageService.ouvertureCompte(stringOperationDTOErrorCode, dto.getFormulaire(), dto.getRectoID(), dto.getVerso(),CANAL);
    }

    @Test(expected = BadRequestAlertException.class)
    public void testOuvertureCompteNotValideImage() throws Exception {

        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);

        urgenceDepannageService.ouvertureCompte(stringOperationDTO, operationDTOErrorRecto().getFormulaire(), operationDTOErrorRecto().getRectoID(), operationDTOErrorRecto().getVerso(),CANAL);

    }

    @Test(expected = BadRequestAlertException.class)
    public void testOuvertureCompteNotValideMsisdn() throws Exception {

        Mail mail = getMailEntity();

        when(mailSendRepository.save(any())).thenReturn(mail);

        urgenceDepannageService.ouvertureCompte(stringOperationDTONoValideMsisdn, operationDTOErrorRecto().getFormulaire(), operationDTOErrorRecto().getRectoID(), operationDTOErrorRecto().getVerso(),CANAL);

    }

}
