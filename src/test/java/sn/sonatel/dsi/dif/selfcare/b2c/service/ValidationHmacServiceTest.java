package sn.sonatel.dsi.dif.selfcare.b2c.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SelfcareB2CApp.class})
public class ValidationHmacServiceTest {

    @Autowired
    private ApplicationProperties mockApplicationProperties;

    private ValidationHmacService validationHmacServiceUnderTest;

    @Autowired
    private SHA256Handler sha256Handler;

    private static final String MSISDN = "771326617";

    private static final String HMAC = "5f05b0c52dd671a35c0e6cba04ed8ed0d76a35f675b5a9b30c2791aa1cfb8d75";

    private static final String UUID = "789";

    @Before
    public void setUp() {
        initMocks(this);
        validationHmacServiceUnderTest = new ValidationHmacService(mockApplicationProperties);
    }

    @Test
    public void testValidateHmacFalse() {


        // Run the test
        final boolean result = validationHmacServiceUnderTest.validateHmac(HMAC, MSISDN, "UUID");

        // Verify the results
        assertFalse(result);
    }
}
