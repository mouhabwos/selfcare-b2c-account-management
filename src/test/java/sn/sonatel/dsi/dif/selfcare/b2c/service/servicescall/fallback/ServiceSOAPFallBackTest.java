package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import feign.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ServiceSOAPFallBackTest {

    @Mock
    private ServiceSOAPFallBack serviceSOAPFallBackUnderTest;



    /**
     *  ---------------------- TEST getAbonne method ---------------------------
     */



    @Test
    public void testGetAbonneStatusOK() {
        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(200)
            .headers(headers)
            .build();

        ResponseEntity<AbonneDTO> expectedResult =  ResponseEntity.status(HttpStatus.OK).build();

        serviceSOAPFallBackUnderTest = new ServiceSOAPFallBack(FeignException.errorStatus("SOAP", response));

        ResponseEntity<AbonneDTO> abonne = serviceSOAPFallBackUnderTest.getAbonne(new SOAPRequest(""));

        assertEquals(abonne,expectedResult);
    }



    /**
     *  ---------------------- Status Bad request Test ---------------------------
     */


    @Test
    public void getAbonneError400(){

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(400)
            .headers(headers)
            .build();

        ResponseEntity<AbonneDTO> expectedResult =  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        serviceSOAPFallBackUnderTest = new ServiceSOAPFallBack(FeignException.errorStatus("SOAP", response));

        ResponseEntity<AbonneDTO> abonne = serviceSOAPFallBackUnderTest.getAbonne(new SOAPRequest(""));

        assertEquals(abonne,expectedResult);

    }

    /**
     * code erreur 500
     */
    @Test
    public void getAbonneError500(){

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(500)
            .headers(headers)
            .build();
        ResponseEntity<AbonneDTO> expected =  ResponseEntity.status(500).build();

        serviceSOAPFallBackUnderTest = new ServiceSOAPFallBack(FeignException.errorStatus("SOAP", response));

        ResponseEntity<AbonneDTO> abonne = serviceSOAPFallBackUnderTest.getAbonne(new SOAPRequest(""));

    }

    /**
     * code erreur 503
     */
    @Test
    public void getAbonneError503(){

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(503)
            .headers(headers)
            .build();

        ResponseEntity<AbonneDTO> expectedResult =  ResponseEntity.status(HttpStatus.OK).build();

        serviceSOAPFallBackUnderTest = new ServiceSOAPFallBack(FeignException.errorStatus("SOAP", response));

        ResponseEntity<AbonneDTO> abonne = serviceSOAPFallBackUnderTest.getAbonne(new SOAPRequest(""));

        assertEquals(abonne,expectedResult);

    }





}
