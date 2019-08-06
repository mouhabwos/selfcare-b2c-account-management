package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import feign.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */
public class CustomerOfferClientFallbackTest {

    @Mock
    private Throwable mockThrowable;

    private CustomerOfferClientFallback customerOfferClientFallbackUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        customerOfferClientFallbackUnderTest = new CustomerOfferClientFallback(mockThrowable);
    }

    @Test
    public void getGatewayError400() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(400)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");

        customerOfferClientFallbackUnderTest = new CustomerOfferClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<CustomerOffer> numeroClient = customerOfferClientFallbackUnderTest.customerOffer("7789564563");

        assertEquals(expectedResult,numeroClient);


    }

    @Test
    public void getGatewayError404() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(404)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.NOT_FOUND).body("");

        customerOfferClientFallbackUnderTest = new CustomerOfferClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<CustomerOffer> numeroClient = customerOfferClientFallbackUnderTest.customerOffer("7789564563");

        assertEquals(expectedResult,numeroClient);

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

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        customerOfferClientFallbackUnderTest = new CustomerOfferClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<CustomerOffer> numeroClient = customerOfferClientFallbackUnderTest.customerOffer("7789564563");

        assertEquals(expectedResult,numeroClient);

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

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        customerOfferClientFallbackUnderTest = new CustomerOfferClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<CustomerOffer> numeroClient = customerOfferClientFallbackUnderTest.customerOffer("7789564563");

        assertEquals(expectedResult,numeroClient);

    }

    /**
     * code erreur 401
     */
    @Test
    public void getGatewayError401() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(401)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");

        customerOfferClientFallbackUnderTest = new CustomerOfferClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<CustomerOffer> numeroClient = customerOfferClientFallbackUnderTest.customerOffer("7789564563");

        assertEquals(expectedResult,numeroClient);

    }
    /**
     * code erreur 403
     */
    @Test
    public void getGatewayError403() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(403)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.FORBIDDEN).body("");

        customerOfferClientFallbackUnderTest = new CustomerOfferClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<CustomerOffer> numeroClient = customerOfferClientFallbackUnderTest.customerOffer("7789564563");

        assertEquals(expectedResult,numeroClient);

    }

    /**
     * code  500
     */
    @Test
    public void getGatewayErrorInternalServerError() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(200)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");

        customerOfferClientFallbackUnderTest = new CustomerOfferClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<CustomerOffer> numeroClient = customerOfferClientFallbackUnderTest.customerOffer("7789564563");

        assertEquals(expectedResult,numeroClient);

    }
}
