package sn.sonatel.dsi.dif.selfcare.b2c.service;

import io.github.jhipster.config.JHipsterProperties;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.OperationDTO;


import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class MailService {


    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private final ApplicationProperties applicationProperties;

    private static final String EMAIL_ADMIN = "emailAdmin";

    private static final String BASE_URL = "baseUrl";

    private static final String EMAIL_ACTIVATION_TITLE = "email.activation.title";

    private static final String RANDOM = "random";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine, ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom(),"Service Client Orange Business ");
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {

            log.warn("Email could not be sent to user '{}'", to, e);

        }
    }

    @Async
    public void sendEmailWithAttachement(String to, String subject, String content, boolean isMultipart, boolean isHtml, OperationDTO operationDTO) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {

            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(applicationProperties.getSelfcareMail().getSenderAddress(),applicationProperties.getSelfcareMail().getSenderName());
            message.setSubject(subject);
            message.setText(content, isHtml);
            FileSystemResource fileZip = getFileZip(operationDTO.getNameFile());
            message.addAttachment(operationDTO.getNameFile(), fileZip);

            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            log.warn("Email could not be sent to user '{}'", to, e);

        }
    }

    @Async
    public void sendEmailFromTemplate(AccountB2C user, String templateName, String titleKey) {
        Locale locale = Locale
            .forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(EMAIL_ADMIN, applicationProperties.getEmailAdmin());
        String random = RandomStringUtils.randomAlphabetic(20);
        context.setVariable(RANDOM, random);
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    /**
     * Mail sent to the user to activate his account  when he create his account himself
     * @param user
     */
    @Async
    public void sendActivationEmail(AccountB2C user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", EMAIL_ACTIVATION_TITLE);
    }

    @Async
    public void sendEmailToServiceClient(OperationDTO operationDTO, String templateName, String subject) {
        Locale locale = Locale
            .forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(USER, operationDTO);

        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(EMAIL_ADMIN, applicationProperties.getEmailAdmin());
        String random = RandomStringUtils.randomAlphabetic(20);
        context.setVariable(RANDOM, random);
        String content = templateEngine.process(templateName, context);

        sendEmailWithAttachement(applicationProperties.getEmailServiceClientOrange(), subject, content, true, true, operationDTO);
    }

    /**
     * Mail sent to the user to activate his account  when he create his account himself
     * @param operationDTO
     */
    @Async
    public void sendEmailToServiceClient(OperationDTO operationDTO, String titreOperation) {
        log.debug("Sending activation email to '{}'", "");
        sendEmailToServiceClient(operationDTO, "mail/ouverturCompteEmail", "[Orange et Moi ]"+titreOperation+"- "+operationDTO.getNumero());
    }

    private FileSystemResource getFileZip(String nameFile){
        String tempFile = applicationProperties.getTmpPath()+nameFile;
        return new FileSystemResource(tempFile);
    }
}
