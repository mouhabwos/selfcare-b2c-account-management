package sn.sonatel.dsi.dif.selfcare.b2c.service;

import io.github.jhipster.config.JHipsterProperties;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.spring5.SpringTemplateEngine;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserInfoOuvertureCompte;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServiceFile;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = SelfcareB2CApp.class)
public class MailServiceIntTest {

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

        private MailService mailService;

        @Autowired
        private ApplicationProperties applicationProperties;

        @Autowired
        private ServiceFile service;

        @Mock
        private JHipsterProperties mockJHipsterProperties;
        @Mock
        private JavaMailSender mockJavaMailSender;
        @Mock
        private MessageSource mockMessageSource;
        @Mock
        private SpringTemplateEngine mockTemplateEngine;
        @Mock
        private ApplicationProperties mockApplicationProperties;
        @Mock
        private ServiceFile mockService;


    private MailService mailServiceUnderTest;

        @Before
        public void setup() {
            MockitoAnnotations.initMocks(this);
            doNothing().when(javaMailSender).send(any(MimeMessage.class));
            mailService = new MailService(jHipsterProperties, javaMailSender, messageSource, templateEngine, applicationProperties, service);
            mailServiceUnderTest = new MailService(mockJHipsterProperties, mockJavaMailSender, mockMessageSource, mockTemplateEngine, mockApplicationProperties, mockService);

        }

        @Test
        public void testSendEmail() throws Exception {
            mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, false);
            verify(javaMailSender).send(messageCaptor.capture());
            MimeMessage message = messageCaptor.getValue();
            assertThat(message.getSubject()).isEqualTo("testSubject");
            assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
            assertThat(message.getContent()).isInstanceOf(String.class);
            assertThat(message.getContent().toString()).isEqualTo("testContent");
            assertThat(message.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
        }

        @Test
        public void testSendHtmlEmail() throws Exception {
            mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", false, true);
            verify(javaMailSender).send(messageCaptor.capture());
            MimeMessage message = messageCaptor.getValue();
            assertThat(message.getSubject()).isEqualTo("testSubject");
            assertThat(message.getContent().toString()).isEqualTo("testContent");
            assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        }

        @Test
        public void testSendMultipartEmail() throws Exception {
            mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, false);
            verify(javaMailSender).send(messageCaptor.capture());
            MimeMessage message = messageCaptor.getValue();
            MimeMultipart mp = (MimeMultipart) message.getContent();
            MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
            ByteArrayOutputStream aos = new ByteArrayOutputStream();
            part.writeTo(aos);
            assertThat(message.getSubject()).isEqualTo("testSubject");
            assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
            assertThat(message.getContent()).isInstanceOf(Multipart.class);
            assertThat(aos.toString()).isEqualTo("\r\ntestContent");
            assertThat(part.getDataHandler().getContentType()).isEqualTo("text/plain; charset=UTF-8");
        }

        @Test
        public void testSendMultipartHtmlEmail() throws Exception {
            mailService.sendEmail("john.doe@example.com", "testSubject", "testContent", true, true);
            verify(javaMailSender).send(messageCaptor.capture());
            MimeMessage message = messageCaptor.getValue();
            MimeMultipart mp = (MimeMultipart) message.getContent();
            MimeBodyPart part = (MimeBodyPart) ((MimeMultipart) mp.getBodyPart(0).getContent()).getBodyPart(0);
            ByteArrayOutputStream aos = new ByteArrayOutputStream();
            part.writeTo(aos);
            assertThat(message.getSubject()).isEqualTo("testSubject");
            assertThat(message.getAllRecipients()[0].toString()).isEqualTo("john.doe@example.com");
            assertThat(aos.toString()).isEqualTo("\r\ntestContent");
            assertThat(part.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        }

        @Test
        public void testSendEmailFromTemplate() throws Exception {
            AccountB2C user = new AccountB2C();
            user.setNumero("774255555");
            user.setEmail("bouyakandee@outlook.com");
            user.setLangKey("en");
            mailService.sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
            verify(javaMailSender).send(messageCaptor.capture());
            MimeMessage message = messageCaptor.getValue();
            assertThat(message.getSubject()).isEqualTo("Activation de votre compte selfcare-b2c-account-management");
            assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
            assertThat(message.getContent().toString()).isNotEmpty();
            assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        }

        @Test
        public void testSendActivationEmail() throws Exception {
            AccountB2C user = new AccountB2C();
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
            user.setNumero("774565252");
            user.setEmail("bouyakandee@outlook.com");
            mailService.sendActivationEmail(user);
            verify(javaMailSender).send(messageCaptor.capture());
            MimeMessage message = messageCaptor.getValue();
            assertThat(message.getAllRecipients()[0].toString()).isEqualTo(user.getEmail());
            assertThat(message.getContent().toString()).isNotEmpty();
            assertThat(message.getDataHandler().getContentType()).isEqualTo("text/html;charset=UTF-8");
        }



    @Test
    public void testSendEmailToServiceClient() throws Exception {
      UserInfoOuvertureCompte user = new UserInfoOuvertureCompte();
        user.setNumero("771326617");
        user.setFirstName("bouya");
        user.setLastName("kande");
        user.setOperation("Ouverture compte OM");
        user.setFormulaire("formulaire_inscription_om_original.pdf");
        user.setRectoID("1.PNG");
        user.setVersoID("1.PNG");
        mailService.sendEmailToServiceClient(user, "mail/ouverturCompteEmail","email.activation.title");

    }


  @Test
    public void testSendEmailFromServiceClientWithException() throws Exception {
        UserInfoOuvertureCompte user = new UserInfoOuvertureCompte();
        user.setNumero("771326617");
        user.setFirstName("bouya");
        user.setLastName("kande");
        user.setOperation("Ouverture compte OM");
        user.setFormulaire("formulaire_inscription_om_original.pdf");
        user.setRectoID("1.PNG");
        user.setVersoID("1.PNG");
        user.setObjectVersoID(null);
        user.setObjectFormulaire(null);
        user.setObjectRectoID(null);
        doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
        mailService.sendEmailWithAttachement("john.doe@example.com", "testSubject", "testContent", true, false, user);

    }


    public Resource recupFile() throws Exception {
        File file = new File("pom.xml");
        String path = file.getAbsolutePath();
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return resource;
    }

        @Test
        public void testSendEmailWithException() throws Exception {

            doThrow(MailSendException.class).when(javaMailSender).send(any(MimeMessage.class));
            UserInfoOuvertureCompte user = new UserInfoOuvertureCompte();
            File file = new File("pom.xml");
            String path = file.getAbsolutePath();
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            user.setNumero("771326617");
            user.setFirstName("bouya");
            user.setLastName("kande");
            user.setOperation("Ouverture compte OM");
            user.setFormulaire("formulaire_inscription_om_original.pdf");
            user.setRectoID("1.PNG");
            user.setVersoID("1.PNG");
            user.setObjectVersoID(recupFile());
            user.setObjectFormulaire(recupFile());
            user.setObjectRectoID(recupFile());
            user.setNumero("774565252");

            //when(mailServiceUnderTest.sendEmailWithAttachement((Constants.EMAIL_SERVICE_CLIENT,"testSubject", "testContent", true, true, user)).getMock());

            mailService.sendEmailWithAttachement(Constants.EMAIL_SERVICE_CLIENT,"testSubject", "testContent", true, true, user);


        }

    @Test
    public void testSendEmailWithAttachement() throws Exception {
        // Setup
        final String to = "to";
        final String subject = "subject";
        final String content = "content";
        final boolean isMultipart = false;
        final boolean isHtml = false;
        final UserInfoOuvertureCompte user = new UserInfoOuvertureCompte();
        user.setNumero("771326617");
        user.setFirstName("bouya");
        user.setLastName("kande");
        user.setOperation("Ouverture compte OM");
        user.setFormulaire("formulaire_inscription_om_original.pdf");
        user.setRectoID("1.PNG");
        user.setVersoID("1.PNG");
        user.setObjectVersoID(recupFile());
        user.setObjectFormulaire(recupFile());
        user.setObjectRectoID(recupFile());
        user.setNumero("774565252");

        // Run the test
        mailServiceUnderTest.sendEmailWithAttachement(to, subject, content, isMultipart, isHtml, user);

        // Verify the results
    }

    @Test
    public void sendEmailFromServiceClient() throws Exception {
        UserInfoOuvertureCompte user = new UserInfoOuvertureCompte();

        user.setNumero("771326617");
        user.setFirstName("bouya");
        user.setLastName("kande");
        user.setOperation("Ouverture compte OM");
        user.setFormulaire("formulaire_inscription_om_original.pdf");
        user.setRectoID("1.PNG");
        user.setVersoID("1.PNG");
        user.setObjectVersoID(null);
        user.setObjectFormulaire(null);
        user.setObjectRectoID(null);
        user.setNumero("774565252");
        mailService.sendEmailFromServiceClient(user);
    }

}
