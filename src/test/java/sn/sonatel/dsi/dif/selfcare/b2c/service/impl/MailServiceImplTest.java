package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.Mail;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.StatusMail;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.MailSendRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MailServiceImplTest {

    @Mock
    private MailSendRepository mailSendRepository;

    private MailSendServiceImpl mailSendServiceImplUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        mailSendServiceImplUnderTest = new MailSendServiceImpl(mailSendRepository);
    }

    @Test
    public void testGetStatusMailSend() {
        // Setup
        final String idRequest = "mail_1234566";

        Mail mail = new Mail();
        mail.setStatus(StatusMail.IN_PROGRESS);
        mail.setIdRequest("mail_1234566");
        mail.setId(1L);
        Optional<Mail> request = Optional.ofNullable(mail);
        when(mailSendRepository.findByIdRequest(anyString())).thenReturn(request);

        // Run the test
        final String result = mailSendServiceImplUnderTest.getStatusMailSend(idRequest);

        // Verify the results
        assertEquals(mail.getStatus()+"", result);
    }

    @Test(expected =  BadRequestAlertException.class)
    public void testGetStatusMailSendThrowsBadRequestAlertException() {

        final String idRequest = "mail_1234566";

        mailSendServiceImplUnderTest.getStatusMailSend(idRequest);
    }
}
