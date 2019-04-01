package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SelfcareSoapServiceTest {


    private SelfcareSoapService soapService;

    //@Autowired
   // @Qualifier("loadBalancedRestTemplate")
    @Mock
    //@Autowired
    private RestTemplate restTemplate;



    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        soapService = new SelfcareSoapService(restTemplate);

    }

    @Test
    public void getSouscription() {
        ResponseEntity<SouscriptionDto> response = ResponseEntity.status(HttpStatus.OK).build();

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest("771326617"));
        //Mockito.any()
        when(soapService.getSouscription(request)).thenReturn(response);

        ResponseEntity<SouscriptionDto> entity = soapService.getSouscription(request);

        //System.out.println("===========<<<<<<>>>>>> "+entity.getStatusCode());

        assertTrue(entity.equals(response));
    }

    @Test
    public void getSouscriptionServiceUnavailable() {
        ResponseEntity<SouscriptionDto> response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest("771326617"));
        //Mockito.any()
        when(soapService.getSouscription(request)).thenReturn(response);

        ResponseEntity<SouscriptionDto> entity = soapService.getSouscription(request);

        assertTrue(entity.equals(response));
    }



    @Test
    public void getAbonne() {
        ResponseEntity<List<AbonneDTO>> response = ResponseEntity.status(HttpStatus.OK).build();

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest("771326617"));
        //Mockito.any()
        when(soapService.getAbonne(request)).thenReturn(response);

        ResponseEntity<List<AbonneDTO>> entity = soapService.getAbonne(request);

        assertTrue(entity.equals(response));
    }

    @Test
    public void getAbonneServiceUnavailable() {
        ResponseEntity<List<AbonneDTO>> response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest("771326617"));
        //Mockito.any()
        when(soapService.getAbonne(request)).thenReturn(response);

        ResponseEntity<List<AbonneDTO>> entity = soapService.getAbonne(request);

        assertTrue(entity.equals(response));
    }

}
