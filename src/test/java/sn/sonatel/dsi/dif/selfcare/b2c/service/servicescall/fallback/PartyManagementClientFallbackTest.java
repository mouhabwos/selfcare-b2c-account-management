package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import feign.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.CustomerOffer;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.IndividualInformation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PartyManagementClientFallbackTest {

    @Mock
    private Throwable mockThrowable;

    private PartyManagementClientFallback partyManagementClientFallbackUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        partyManagementClientFallbackUnderTest = new PartyManagementClientFallback(mockThrowable);
    }


    @Test
    public void getGatewayError400() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(400)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");

        partyManagementClientFallbackUnderTest = new PartyManagementClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<IndividualInformation> numeroClient = partyManagementClientFallbackUnderTest.getIndividualInformation("7789564563");

        assertEquals(expectedResult,numeroClient);


    }

    @Test
    public void getGatewayError404() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(404)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.NOT_FOUND).body("");

        partyManagementClientFallbackUnderTest = new PartyManagementClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<IndividualInformation> numeroClient = partyManagementClientFallbackUnderTest.getIndividualInformation("7789564563");

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

        partyManagementClientFallbackUnderTest = new PartyManagementClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<IndividualInformation> numeroClient = partyManagementClientFallbackUnderTest.getIndividualInformation("7789564563");

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

        partyManagementClientFallbackUnderTest = new PartyManagementClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<IndividualInformation> numeroClient = partyManagementClientFallbackUnderTest.getIndividualInformation("7789564563");

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

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        partyManagementClientFallbackUnderTest = new PartyManagementClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<IndividualInformation> numeroClient = partyManagementClientFallbackUnderTest.getIndividualInformation("7789564563");

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

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        partyManagementClientFallbackUnderTest = new PartyManagementClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<IndividualInformation> numeroClient = partyManagementClientFallbackUnderTest.getIndividualInformation("7789564563");

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

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        partyManagementClientFallbackUnderTest = new PartyManagementClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<IndividualInformation> numeroClient = partyManagementClientFallbackUnderTest.getIndividualInformation("7789564563");

        assertEquals(expectedResult,numeroClient);

    }

    @Test
    public void getGatewayTimeOut() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(504)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("");

        partyManagementClientFallbackUnderTest = new PartyManagementClientFallback(FeignException.errorStatus("api", response));

        ResponseEntity<IndividualInformation> numeroClient = partyManagementClientFallbackUnderTest.getIndividualInformation("7789564563");

        assertEquals(expectedResult,numeroClient);

    }
}
