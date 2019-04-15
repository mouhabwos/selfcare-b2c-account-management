package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.OTPService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SelfcareSoapServiceTest {

    private static final String DEFAULT_NUMERO = "771326617";
    private static final String DEFAULT_CODE = "123456";

    @Mock
    private SelfcareSoapService soapService;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
       // soapService = new SelfcareSoapService(restTemplate,otpService);

    }

    @Test
    public void getSouscription() {

        ResponseEntity<SouscriptionDto> response = ResponseEntity.status(HttpStatus.OK).build();

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest(DEFAULT_NUMERO));

        when(soapService.getSouscription(request)).thenReturn(response);

        ResponseEntity<SouscriptionDto> entity = soapService.getSouscription(request);

        assertTrue(entity.equals(response));
    }

    @Test
    public void getSouscriptionServiceUnavailable() {

        ResponseEntity<SouscriptionDto> response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest(DEFAULT_NUMERO));

        when(soapService.getSouscription(request)).thenReturn(response);

        ResponseEntity<SouscriptionDto> entity = soapService.getSouscription(request);

        assertTrue(entity.equals(response));
    }

    @Test
    public void getAbonne() {

        ResponseEntity<List<AbonneDTO>> response = ResponseEntity.status(HttpStatus.OK).build();

        Mockito.when(soapService.getAbonne(Mockito.anyString(),Mockito.anyString())).thenReturn(response);

        ResponseEntity<List<AbonneDTO>> entity = soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE);

        assertTrue(HttpStatus.OK.equals(entity.getStatusCode()));
    }

    @Test
    public void getAbonneServiceUnavailable() {

        ResponseEntity<List<AbonneDTO>> response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest(DEFAULT_NUMERO));


        when(soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE)).thenReturn(response);

        ResponseEntity<List<AbonneDTO>> entity = soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE);

        assertTrue(entity.equals(response));
    }

}
