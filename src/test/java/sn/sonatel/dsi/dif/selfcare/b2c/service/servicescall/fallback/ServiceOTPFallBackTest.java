package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import feign.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.vm.MessageVM;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

public class ServiceOTPFallBackTest {

    @Mock
    private Throwable mockThrowable;

    private ServiceOTPFallBack serviceOTPFallBackUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        serviceOTPFallBackUnderTest = new ServiceOTPFallBack(mockThrowable);
    }

    /**
     * ---------------------- Status Bad request Test ---------------------------
     */


    @Test
    public void getDownloadFileError400() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(400)
            .headers(headers)
            .build();

        ResponseEntity<Resource> expectedResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        serviceOTPFallBackUnderTest = new ServiceOTPFallBack(FeignException.errorStatus("OTP", response));

        ResponseEntity<CodeOTPCheckDTO> file = serviceOTPFallBackUnderTest.checkOTP(new CodeOTPCheckDTO());



    }

    /**
     * code erreur 500
     */
    @Test
    public void gownloadFileError500() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(500)
            .headers(headers)
            .build();

        ResponseEntity<Resource> expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        serviceOTPFallBackUnderTest = new ServiceOTPFallBack(FeignException.errorStatus("OTP", response));

        ResponseEntity<CodeOTPCheckDTO> file = serviceOTPFallBackUnderTest.checkOTP(new CodeOTPCheckDTO());




    }

    /**
     * code erreur 503
     */
    @Test
    public void downloadFileError503() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(503)
            .headers(headers)
            .build();

        ResponseEntity<Resource> expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        serviceOTPFallBackUnderTest = new ServiceOTPFallBack(FeignException.errorStatus("OTP", response));

        ResponseEntity<CodeOTPCheckDTO> file = serviceOTPFallBackUnderTest.checkOTP(new CodeOTPCheckDTO());


    }
}
