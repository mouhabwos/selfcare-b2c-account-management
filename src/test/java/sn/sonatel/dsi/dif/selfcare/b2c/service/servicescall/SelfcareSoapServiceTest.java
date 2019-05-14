package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.service.OTPService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class SelfcareSoapServiceTest {

    private static final String DEFAULT_NUMERO = "771326617";
    private static final String DEFAULT_CODE = "123456";

    @InjectMocks
    private SelfcareSoapService soapService;

    @Mock
    private OTPService otpService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ServiceSOAP serviceSOAP;

    @Before
    public void setUp() throws Exception {

       //soapService = new SelfcareSoapService(restTemplate, otpService);
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void getSouscription() {

        ResponseEntity<SouscriptionDto> response = ResponseEntity.status(HttpStatus.OK).build();

        when(serviceSOAP.getSouscription(any())).thenReturn(response);

        //when(soapService.getSouscription(DEFAULT_NUMERO)).thenReturn(response);

        ResponseEntity<SouscriptionDto> entity = soapService.getSouscription(DEFAULT_NUMERO);

       // assertTrue(entity.equals(response));
    }


    @Test
    public void getAbonne() {

        CodeOTPCheckDTO codeOTPCheckDTO = new CodeOTPCheckDTO();
        codeOTPCheckDTO.setValid(true);
        codeOTPCheckDTO.setMsisdn(DEFAULT_NUMERO);
        codeOTPCheckDTO.setCode(DEFAULT_CODE);
        otpService = mock(OTPService.class);
        when(otpService.checkOPT(DEFAULT_NUMERO, DEFAULT_CODE)).thenReturn(codeOTPCheckDTO);
       // soapService = mock(SelfcareSoapService.class);

           ResponseEntity<AbonneDTO> response = ResponseEntity.status(HttpStatus.OK).build();
         when(soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE)).thenReturn(response);

       ResponseEntity<AbonneDTO> entity = soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE);
        System.out.println(entity);
      //  verify(otpService).checkOPT(DEFAULT_NUMERO,DEFAULT_CODE);
        verify(soapService).getAbonne(DEFAULT_NUMERO,DEFAULT_CODE);

        //assertTrue(entity.equals(response));
    }

    @Test
    public void getAbonneServiceUnavailable() {

        ResponseEntity<AbonneDTO> entity = soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE);

        assertTrue(HttpStatus.SERVICE_UNAVAILABLE.equals(entity.getStatusCode()));
    }

}
