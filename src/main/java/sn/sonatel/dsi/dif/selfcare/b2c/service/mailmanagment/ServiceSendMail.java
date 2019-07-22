package sn.sonatel.dsi.dif.selfcare.b2c.service.mailmanagment;

import io.github.jhipster.config.JHipsterProperties;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.StatusMail;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.MailSendRepository;

import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 */

@Service
public class ServiceSendMail {


    private final Logger log = LoggerFactory.getLogger(ServiceSendMail.class);

    private static final String USER = "user";

    private final ApplicationProperties applicationProperties;

    private static final String EMAIL_ADMIN = "emailAdmin";

    private static final String BASE_URL = "baseUrl";

    private static final String RANDOM = "random";

    private static final String TEMPLATE_NAME = "mail/ouverturCompteEmail";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    private final MailSendRepository mailSendRepository;

    public ServiceSendMail(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, ApplicationProperties applicationProperties, MailSendRepository mailSendRepository) {
        this.applicationProperties = applicationProperties;
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.mailSendRepository = mailSendRepository;
    }


    @Async
    public void sendEmailToServiceClient(Map<String, DataSource> sourceList, Mail mail) throws IOException{
        log.debug("Sending activation email to '{}'", "");
        sendEmailToServiceClient(sourceList ,mail, TEMPLATE_NAME, "["+applicationProperties.getSelfcareMail().getMailSubject()+"]  "+mail.getOperationTitre()+" - "+mail.getNumero());
    }

    @Async
    public void sendEmailToServiceClient(Map<String, DataSource> sourceList, Mail mail, String templateName, String subject) throws IOException {
        Locale locale = Locale
            .forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(USER, mail);

        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(EMAIL_ADMIN, applicationProperties.getEmailAdmin());
        String random = RandomStringUtils.randomAlphabetic(20);
        context.setVariable(RANDOM, random);
        String content = templateEngine.process(templateName, context);

        sendEmailWithAttachement(sourceList, subject, content, true, true,  mail);
    }


    @Async
    public void sendEmailWithAttachement(Map<String, DataSource> dataSourceMap,  String subject, String content, boolean isMultipart, boolean isHtml, Mail mail) throws IOException {
        log.debug("Send email[multipart '{}' and html '{}'] with subject '{}' and content={}",
            isMultipart, isHtml,  subject, content);



        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {

            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(applicationProperties.getEmailServiceClientOrange());
            message.setFrom(applicationProperties.getSelfcareMail().getSenderAddress(),applicationProperties.getSelfcareMail().getSenderName());
            message.setSubject(subject);
            message.setText(content,isHtml);

            dataSourceMap.forEach((fileName,dataSource)->{

                try {
                        message.addAttachment(fileName, dataSource);

                    } catch (MessagingException e) {
                        log.debug("Error add attachement");
                    }

                });

            Transport.send(mimeMessage);

            log.debug("Sent email to User '{}'", applicationProperties.getEmailServiceClientOrange());
        } catch (Exception e) {
            mail.setStatus(StatusMail.FAILED);
            mailSendRepository.save(mail);
            log.warn("Email could not be sent to user '{}'", applicationProperties.getEmailServiceClientOrange(), e);

        }


    }


    public String getTitleOperation(String codeOperation){

        String title = "";

        for (ApplicationProperties.UrgenceDepannage.Operation operation1:
            applicationProperties.getUrgenceDepannage().getOperation()){
            if(operation1.getCode().equals(codeOperation)){
                title = operation1.getTitle();

            }

        }
        return title;
    }


}
