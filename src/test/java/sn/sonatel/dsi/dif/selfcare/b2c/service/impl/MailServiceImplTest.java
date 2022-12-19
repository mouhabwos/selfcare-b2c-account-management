package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.StatusMail;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.MailSendRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LoginAlreadyUsedException;

class MailServiceImplTest {

    @Mock
    private MailSendRepository mailSendRepository;

    private MailSendServiceImpl mailSendServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        mailSendServiceImplUnderTest = new MailSendServiceImpl(mailSendRepository);
    }

    @Test
    void testGetStatusMailSend() {
        // Setup
        final String idRequest = "mail_1234566";

        Mail mail = new Mail();
        mail.setStatus(StatusMail.IN_PROGRESS);
        mail.setIdRequest("mail_1234566");
        mail.setId(1L);
        Optional<Mail> request = Optional.of(mail);
        when(mailSendRepository.findByIdRequest(anyString())).thenReturn(request);

        // Run the test
        final String result = mailSendServiceImplUnderTest.getStatusMailSend(idRequest);

        // Verify the results
        Assertions.assertEquals(mail.getStatus() + "", result);
    }

    @Test
    void testGetStatusMailSendThrowsBadRequestAlertException() {
        final String idRequest = "mail_1234566";

        BadRequestAlertException thrown = org.junit.jupiter.api.Assertions.assertThrows(
            BadRequestAlertException.class,
            () -> {
                mailSendServiceImplUnderTest.getStatusMailSend(idRequest);
            },
            "BadRequestAlertException was expected"
        );

        org.junit.jupiter.api.Assertions.assertEquals("idRequest non Trouve", thrown.getTitle());
    }
}
