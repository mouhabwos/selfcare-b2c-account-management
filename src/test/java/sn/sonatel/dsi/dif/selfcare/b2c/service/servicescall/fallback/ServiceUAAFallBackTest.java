package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.fallback;

import feign.FeignException;
import feign.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class ServiceUAAFallBackTest {


    @Mock
    private ServiceUAAFallBack serviceUAAFallBackUnderTest;



    @Test
    public void createUAAStatusOK() {
        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(200)
            .headers(headers)
            .build();


        serviceUAAFallBackUnderTest = new ServiceUAAFallBack(FeignException.errorStatus("UAA", response));

        serviceUAAFallBackUnderTest.register(new ManagedUserVM());

    }



    /**
     *  ---------------------- Status Bad request Test ---------------------------
     */


    @Test
    public void createUAAError400(){

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(400)
            .headers(headers)
            .build();

        serviceUAAFallBackUnderTest = new ServiceUAAFallBack(FeignException.errorStatus("UAA", response));

        serviceUAAFallBackUnderTest.register(new ManagedUserVM());

    }

    /**
     * code erreur 500
     */
    @Test
    public void createUAAError500(){

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(500)
            .headers(headers)
            .build();

        serviceUAAFallBackUnderTest = new ServiceUAAFallBack(FeignException.errorStatus("UAA", response));

        serviceUAAFallBackUnderTest.register(new ManagedUserVM());

    }

    /**
     * code erreur 503
     */
    @Test
    public void createUAAError503(){

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(503)
            .headers(headers)
            .build();

        serviceUAAFallBackUnderTest = new ServiceUAAFallBack(FeignException.errorStatus("UAA", response));

        serviceUAAFallBackUnderTest.register(new ManagedUserVM());

    }


}
