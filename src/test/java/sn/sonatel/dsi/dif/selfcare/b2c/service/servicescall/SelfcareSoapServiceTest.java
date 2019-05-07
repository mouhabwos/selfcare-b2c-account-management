package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Before
    public void setUp() throws Exception {

       soapService = new SelfcareSoapService(restTemplate, otpService);
        MockitoAnnotations.initMocks(this);

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


        CodeOTPCheckDTO codeOTPCheckDTO = new CodeOTPCheckDTO();
        codeOTPCheckDTO.setValid(true);
        codeOTPCheckDTO.setMsisdn(DEFAULT_NUMERO);
        codeOTPCheckDTO.setCode(DEFAULT_CODE);


        //  ResponseEntity<AbonneDTO> response = ResponseEntity.status(HttpStatus.OK).build();


//        Mockito.when(otpService.checkOPT(Mockito.anyString(),Mockito.anyString())).thenReturn(codeOTPCheckDTO);
       // Mockito.when(otpService.checkOPT(Mockito.anyString(),Mockito.anyString())).thenReturn(response);


       /* Mockito.when(restTemplate.exchange(
            Matchers.eq(" http://selfcare-otp/api/code-otp-infos/check"),
            Matchers.eq(HttpMethod.POST),
            Matchers.<HttpEntity<CodeOTPCheckDTO>>any(),
            Matchers.<ParameterizedTypeReference<CodeOTPCheckDTO>>any())
        ).thenReturn(myEntityCodeOtp);*/




        // call methode getAbonne

       // soapService.getAbonne(DEFAULT_NUMERO, DEFAULT_CODE);

        ResponseEntity<AbonneDTO> response = ResponseEntity.status(HttpStatus.OK).build();

       // doReturn(response).when(soapService).getAbonne(anyString(),anyString());
            //soapService = mock(SelfcareSoapService.class);
        //Mockito.when(soapService.getAbonne(anyString(), anyString())).thenReturn(response);
        given(otpService.checkOPT(anyString(), anyString())).willReturn(codeOTPCheckDTO);

        ResponseEntity<AbonneDTO> entity = soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE);

       // assertTrue(HttpStatus.OK.equals(entity.getStatusCode()));
    }

    @Test
    public void getAbonneServiceUnavailable() {

        ResponseEntity<AbonneDTO> entity = soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE);

        assertTrue(HttpStatus.SERVICE_UNAVAILABLE.equals(entity.getStatusCode()));
    }

}
