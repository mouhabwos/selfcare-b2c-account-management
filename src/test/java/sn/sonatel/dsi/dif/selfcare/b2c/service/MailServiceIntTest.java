package sn.sonatel.dsi.dif.selfcare.b2c.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring5.SpringTemplateEngine;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OperationDTO;
import tech.jhipster.config.JHipsterProperties;

@RunWith(SpringRunner.class)
@IntegrationTest
class MailServiceIntTest {

    //private static final Resource FILE = "";

    @Autowired
    private JHipsterProperties jHipsterProperties;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Spy
    private JavaMailSenderImpl javaMailSender;

    @Captor
    private ArgumentCaptor<MimeMessage> messageCaptor;

    @Autowired
    private MailService mailService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Mock
    private JHipsterProperties mockJHipsterProperties;

    @Mock
    private JavaMailSender mockJavaMailSender;

    @Mock
    private MessageSource mockMessageSource;

    @Mock
    private SpringTemplateEngine mockTemplateEngine;

    @Autowired
    private ApplicationProperties mockApplicationProperties;

    @Mock
    private MailService serviceMailMock;

    private MailService mailServiceUnderTest;

    private OperationDTO userInfoWithResources;

    @BeforeEach
    public void setup() throws Exception {
        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        mailService = new MailService(jHipsterProperties, javaMailSender, messageSource, templateEngine, applicationProperties);
        //mailServiceUnderTest = new MailService(mockJHipsterProperties, mockJavaMailSender, mockMessageSource, mockTemplateEngine, mockApplicationProperties);
        userInfoWithResources = initUserInfoWithResources();
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

    private OperationDTO initUserInfoWithResources() throws Exception {
        Resource resource = new DefaultResourceLoader().getResource("classpath:mail/activationEmail.html");

        OperationDTO user = new OperationDTO();
        user = operationDTO();
        return user;
    }

    @Test
    void testSendEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).hasToString("john.doe@example.com");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).hasToString("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }

    @Test
    void testSendHtmlEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, true);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getContent().toString()).hasToString("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    void testSendMultipartEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, false);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        MimeMultipart mp = (MimeMultipart) message.getContent();
        MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        part.writeTo(aos);
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).hasToString("john.doe@example.com");
        assertThat(message.getContent()).isInstanceOf(Multipart.class);
        assertThat(aos.toString()).hasToString("\r\ntestContent");
        assertThat(part.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
    }

    @Test
    void testSendMultipartHtmlEmail() throws Exception {
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, true);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        MimeMultipart mp = (MimeMultipart) message.getContent();
        MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        part.writeTo(aos);
        assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).hasToString("john.doe@example.com");
        assertThat(aos.toString()).hasToString("\r\ntestContent");
        assertThat(part.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    void testSendEmailFromTemplate() throws Exception {
        AccountB2C user = new AccountB2C();
        user.setNumero("774255555");
        user.setEmail("bouyakandee@outlook.com");
        user.setLangKey("en");
        mailService.sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getSubject()).isEqualTo("Activation de votre compte selfcare-b2c-account-management");
        assertThat(message.getAllRecipients()[0].toString()).hasToString(user.getEmail());
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    void testSendActivationEmail() throws Exception {
        AccountB2C user = new AccountB2C();
        user.setLangKey(Constants.DEFAULT_LANGUAGE);
        user.setNumero("774565252");
        user.setEmail("bouyakandee@outlook.com");
        mailService.sendActivationEmail(user);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        assertThat(message.getAllRecipients()[0].toString()).hasToString(user.getEmail());
        assertThat(message.getContent().toString()).isNotEmpty();
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
    }

    @Test
    void testSendEmailToServiceClient() throws Exception {
        OperationDTO user = operationDTO();
        mailService.sendEmailToServiceClient(user, "mail/ouverturCompteEmail", "Erreur transaction Orange Money");
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendEmailFromServiceClientWithException() throws Exception {
        OperationDTO user = operationDTO();
        user.setFormulaire(null);
        user.setRectoID(null);
        doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
        mailService.sendEmailWithAttachement("john.doe@example.com", "testSubject", "testContent", true, false, user);
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    Resource recupFile() throws Exception {
        File file = new File("pom.xml");
        String path = file.getAbsolutePath() + "/src/test/resources";
        System.out.println("==========> " + file);
        return new InputStreamResource(new FileInputStream(file));
    }

    @Test
    void testSendEmailWithException() throws Exception {
        MailService mailServicem = mock(MailService.class);
        doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
        OperationDTO user = operationDTO();
        user.setNumero("774565252");
        mailService.sendEmailWithAttachement(Constants.EMAIL_SERVICE_CLIENT, "testSubject", "testContent", true, true, user);
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendEmailWithAttachement() throws Exception {
        // Setup
        final String to = "to";
        final String subject = "subject";
        final String content = "content";
        final boolean isMultipart = true;
        final boolean isHtml = false;
        final OperationDTO user = operationDTO();

        // Run the test
        mailService.sendEmailWithAttachement(to, subject, content, isMultipart, isHtml, user);

        // Verify the results
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendEmailToServiceClient() throws Exception {
        OperationDTO user = operationDTO();
        user.setVerso(null);
        user.setFormulaire(null);
        user.setRectoID(null);
        user.setNumero("774565252");
        mailService.sendEmailToServiceClient(user, "Erreur transaction Orange Money");
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendDefaultEmailWithException() throws Exception {
        doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
        mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false);
        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void testSendEmailWithAttachement2() throws Exception {
        mailService.sendEmailWithAttachement("john.doe@example.com", "testSubject", "testContent", true, true, userInfoWithResources);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        /*assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).isEqualTo("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");*/
    }

    @Test
    void testSendEmailWithAttachementOptional2() throws Exception {
        OperationDTO userOptional = new OperationDTO();
        userOptional.setEmail("test@gmail.com");
        userOptional.setFormulaire(null);
        mailService.sendEmailWithAttachement("john.doe@example.com", "testSubject", "testContent", true, true, userOptional);
        verify(javaMailSender).send(messageCaptor.capture());
        MimeMessage message = messageCaptor.getValue();
        /*assertThat(message.getSubject()).isEqualTo("testSubject");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
        assertThat(message.getFrom()[0].toString()).isEqualTo("test@localhost");
        assertThat(message.getContent()).isInstanceOf(String.class);
        assertThat(message.getContent().toString()).isEqualTo("testContent");
        assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");*/
    }
}
