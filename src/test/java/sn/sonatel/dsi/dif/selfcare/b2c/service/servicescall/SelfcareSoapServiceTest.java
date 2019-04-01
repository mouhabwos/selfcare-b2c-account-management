package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.SOAPRequest;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SelfcareSoapServiceTest {

    @Mock
    private SelfcareSoapService soapService;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getSouscription() {
        ResponseEntity<List<AbonneDTO>> response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        HttpEntity<SOAPRequest> request = new HttpEntity<>(new SOAPRequest("771326617"));
        //Mockito.any()
        when(soapService.getAbonne(request)).thenReturn(response);

        ResponseEntity<List<AbonneDTO>> entity = soapService.getAbonne(request);

        assertTrue(entity.getBody().isEmpty());
    }

    @Test
    public void getAbonne() {
    }
}
