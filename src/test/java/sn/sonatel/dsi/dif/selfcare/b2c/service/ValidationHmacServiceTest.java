package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(SpringRunner.class)
 @IntegrationTest
public class ValidationHmacServiceTest {

    @Autowired
    private ApplicationProperties mockApplicationProperties;

    private ValidationHmacService validationHmacServiceUnderTest;

    @Autowired
    private SHA256Handler sha256Handler;

    @Autowired
    private ApplicationProperties applicationProperties;

    private static final String MSISDN = "771326617";

    private static final String HMAC = "5f05b0c52dd671a35c0e6cba04ed8ed0d76a35f675b5a9b30c2791aa1cfb8d75";

    private static final String INDICATIF = "221";

    private static final String UUID = "789";
    private final Date NOW = new Date();

    @BeforeEach
    public void setUp() throws Exception {
        initMocks(this);
        validationHmacServiceUnderTest = new ValidationHmacService(mockApplicationProperties);
        // everytime we call new Date() inside a method of any class
        // declared in @PrepareForTest we will get the NOW instance
        whenNew(Date.class).withAnyArguments().thenReturn(NOW);
    }

    @Test
    public void testValidateHmacFalse() {


        // Run the test
        final boolean result = validationHmacServiceUnderTest.validateHmac(HMAC, MSISDN, "UUID");

        // Verify the results
        assertFalse(result);
    }

    @Test()
    public void testCheckHmacInvalid() {


        ValidationHmacService.InvalidHmacException thrown = org.junit.jupiter.api.Assertions.assertThrows(ValidationHmacService.InvalidHmacException.class, () -> {
            validationHmacServiceUnderTest.checkHmac(HMAC, MSISDN, "UUID");
        }, "BadRequestAlertException was expected");

        org.junit.jupiter.api.Assertions.assertEquals("Hmac non valide", thrown.getMessage());




    }

    @Test
    public void testCheckHmacValid() {

        String hmac= SHA256Handler.encryptSHA256(String.format("%s%s%s%s", INDICATIF+MSISDN, UUID, new SimpleDateFormat("dd/MM/yyyy").format(NOW), applicationProperties.getHmacSecret()));

        // Run the test
        boolean result=validationHmacServiceUnderTest.checkHmac(hmac, MSISDN, UUID);


        // Verify the results
        assertTrue(result);

    }
}
