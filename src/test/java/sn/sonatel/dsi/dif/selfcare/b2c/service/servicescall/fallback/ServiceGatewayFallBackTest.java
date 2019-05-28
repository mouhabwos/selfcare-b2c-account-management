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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;


/**
 *
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
public class ServiceGatewayFallBackTest {

    @Mock
    private Throwable mockThrowable;

    private ServiceGatewayFallBack serviceGatewayFallBackUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        serviceGatewayFallBackUnderTest = new ServiceGatewayFallBack(mockThrowable);
    }


    @Test
    public void getGatewayError400() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(400)
            .headers(headers)
            .build();

        ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        serviceGatewayFallBackUnderTest = new ServiceGatewayFallBack(FeignException.errorStatus("gateway", response));

        ResponseEntity<String> numeroClient = serviceGatewayFallBackUnderTest.getNumeroClient("7789564563");




    }

    @Test
    public void getGatewayError404() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(404)
            .headers(headers)
            .build();

        ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        serviceGatewayFallBackUnderTest = new ServiceGatewayFallBack(FeignException.errorStatus("gateway", response));

        ResponseEntity<String> numeroClient = serviceGatewayFallBackUnderTest.getNumeroClient("778956363");



    }

    /**
     * code erreur 500
     */
    @Test
    public void getGatewayError500() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(500)
            .headers(headers)
            .build();

        ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        serviceGatewayFallBackUnderTest = new ServiceGatewayFallBack(FeignException.errorStatus("gateway", response));

        ResponseEntity<String> numeroClient = serviceGatewayFallBackUnderTest.getNumeroClient("778956363");






    }

    /**
     * code erreur 503
     */
    @Test
    public void getGatewayError503() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(503)
            .headers(headers)
            .build();

        ResponseEntity<String> expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        serviceGatewayFallBackUnderTest = new ServiceGatewayFallBack(FeignException.errorStatus("gateway", response));

        ResponseEntity<String> numeroClient = serviceGatewayFallBackUnderTest.getNumeroClient("778956363");

        //assertEquals(expectedResult,numeroClient);

    }
}
