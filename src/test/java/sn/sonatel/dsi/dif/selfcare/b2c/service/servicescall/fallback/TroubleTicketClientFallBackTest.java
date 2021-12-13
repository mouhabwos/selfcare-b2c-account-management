package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import feign.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.TroubleTicket;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class TroubleTicketClientFallBackTest {

    @Mock
    private Throwable mockThrowable;

    private TroubleTicketClientFallBack troubleTicketClientFallBack;

    @Before
    public void setUp() {
        initMocks(this);
        troubleTicketClientFallBack = new TroubleTicketClientFallBack(mockThrowable);
    }

    @Test
    public void getGatewayError400() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(400)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        troubleTicketClientFallBack = new TroubleTicketClientFallBack(FeignException.errorStatus("api", response));

        ResponseEntity<TroubleTicket> troubleTicket = troubleTicketClientFallBack.getTroubleTicketById("51022830", TroubleTicket.TicketTypeEnum.REQUEST);

        assertEquals(expectedResult, troubleTicket);


    }

    @Test
    public void getGatewayError503() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(503)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);

        troubleTicketClientFallBack = new TroubleTicketClientFallBack(FeignException.errorStatus("api", response));

        ResponseEntity<TroubleTicket> troubleTicket = troubleTicketClientFallBack.getTroubleTicketById("51022830", TroubleTicket.TicketTypeEnum.REQUEST);

        assertEquals(expectedResult, troubleTicket);

    }

    @Test
    public void getTTByMsisdnGatewayError400() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(400)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());

        troubleTicketClientFallBack = new TroubleTicketClientFallBack(FeignException.errorStatus("api", response));

        ResponseEntity<List<TroubleTicket>> troubleTicket = troubleTicketClientFallBack.getTroubleTicketByMsisdn("51022830", TroubleTicket.TicketTypeEnum.REQUEST);

        assertEquals(expectedResult, troubleTicket);


    }

    @Test
    public void getTTByMsisdnGatewayError503() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(503)
            .headers(headers)
            .build();

        ResponseEntity expectedResult = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);

        troubleTicketClientFallBack = new TroubleTicketClientFallBack(FeignException.errorStatus("api", response));

        ResponseEntity<List<TroubleTicket>> troubleTicket = troubleTicketClientFallBack.getTroubleTicketByMsisdn("51022830", TroubleTicket.TicketTypeEnum.REQUEST);

        assertEquals(expectedResult, troubleTicket);

    }
}
