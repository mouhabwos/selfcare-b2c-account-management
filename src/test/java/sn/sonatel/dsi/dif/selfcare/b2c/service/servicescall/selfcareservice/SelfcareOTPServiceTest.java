package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.ServicesOTP;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;

class SelfcareOTPServiceTest {

    @Mock
    private ServicesOTP mockServicesOTP;

    private SelfcareOTPService selfcareOTPServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        selfcareOTPServiceUnderTest = new SelfcareOTPService(mockServicesOTP);
    }

    @Test
    void testCheckOPT() {
        // Setup
        String msisdn = "778565252";
        String code = "code";
        CodeOTPCheckDTO expectedResult = new CodeOTPCheckDTO();
        expectedResult.setCode(code);
        expectedResult.setMsisdn(msisdn);
        expectedResult.setValid(true);

        ResponseEntity<CodeOTPCheckDTO> response = ResponseEntity.status(HttpStatus.OK).body(expectedResult);
        // Run the test

        when(mockServicesOTP.checkOTP(any())).thenReturn(response);

        CodeOTPCheckDTO result = selfcareOTPServiceUnderTest.checkOPT(msisdn, code);
        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testCheckRegisterValidity() {
        // Setup
        final String msisdn = "msisdn";

        Map<String, Boolean> booleanMap = new HashMap<>();
        booleanMap.put("valid", true);

        ResponseEntity<Map<String, Boolean>> mapResponseEntity = ResponseEntity.status(HttpStatus.OK).body(booleanMap);

        when(mockServicesOTP.registerCheckValidRequest(msisdn)).thenReturn(mapResponseEntity);
        // Run the test
        boolean result = selfcareOTPServiceUnderTest.checkRegisterValidity(msisdn);

        // Verify the results
        assertTrue(result);
    }
}
