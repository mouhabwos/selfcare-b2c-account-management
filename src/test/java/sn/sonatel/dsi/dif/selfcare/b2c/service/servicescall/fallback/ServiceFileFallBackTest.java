package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import feign.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class ServiceFileFallBackTest {

    @Mock
    private ServiceFileFallBack serviceFileFallBackUnderTest;

    @Test
    public void testDownloadFileStatusOK() {
        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(200)
            .headers(headers)
            .build();

        ResponseEntity<Resource> expectedResult =  ResponseEntity.status(HttpStatus.OK).build();

        serviceFileFallBackUnderTest = new ServiceFileFallBack(FeignException.errorStatus("FILE", response));

        ResponseEntity<Resource> file = serviceFileFallBackUnderTest.downloadFile("");

        assertEquals(file,expectedResult);
    }



    /**
     *  ---------------------- Status Bad request Test ---------------------------
     */


    @Test
    public void getDownloadFileError400(){

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(400)
            .headers(headers)
            .build();

        ResponseEntity<Resource> expectedResult =  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        serviceFileFallBackUnderTest = new ServiceFileFallBack(FeignException.errorStatus("FILE", response));

        ResponseEntity<Resource> file = serviceFileFallBackUnderTest.downloadFile("");

        assertEquals(file,expectedResult);

    }

    /**
     * code erreur 500
     */
    @Test
    public void gownloadFileError500(){

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(500)
            .headers(headers)
            .build();

        ResponseEntity<Resource> expectedResult =  ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        serviceFileFallBackUnderTest = new ServiceFileFallBack(FeignException.errorStatus("FILE", response));

        ResponseEntity<Resource> file = serviceFileFallBackUnderTest.downloadFile("");

        assertEquals(file,expectedResult);

    }

    /**
     * code erreur 503
     */
    @Test
    public void downloadFileError503(){

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(503)
            .headers(headers)
            .build();

        ResponseEntity<Resource> expectedResult =  ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        serviceFileFallBackUnderTest = new ServiceFileFallBack(FeignException.errorStatus("FILE", response));

        ResponseEntity<Resource> file = serviceFileFallBackUnderTest.downloadFile("");

        assertEquals(file,expectedResult);

    }
}
