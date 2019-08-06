package sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall;

import feign.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.ProfilType;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.AbonneDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.SouscriptionDto;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareOTPService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareSoapService;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class SelfcareSoapServiceTest {

    private static final String DEFAULT_NUMERO = "771326617";
    private static final String DEFAULT_CODE = "123456";


    @InjectMocks
    private SelfcareSoapService soapService;

    @Mock
    private SelfcareOTPService otpService;

    @Mock
    private ServiceSOAP serviceSOAP;


   // @Before
    public void setUp() throws Exception {

       soapService = new SelfcareSoapService(otpService,serviceSOAP);
        initMocks(this);

    }

    @Test
    public void getSouscription() {

        ResponseEntity<SouscriptionDto> response = ResponseEntity.status(HttpStatus.OK).build();

        when(serviceSOAP.getSouscription(any())).thenReturn(response);

        //when(soapService.getSouscription(DEFAULT_NUMERO)).thenReturn(response);

        ResponseEntity<SouscriptionDto> entity = soapService.getSouscription(DEFAULT_NUMERO);

        assertTrue(entity.equals(response));
    }


    @Test
    public void isPostpaid() {
        SouscriptionDto souscriptionDto= new SouscriptionDto();
        souscriptionDto.setProfil(ProfilType.POSTPAID.name());

        ResponseEntity<SouscriptionDto> response = new ResponseEntity<>(souscriptionDto,HttpStatus.OK);

        when(serviceSOAP.getSouscription(any())).thenReturn(response);


        Boolean result = soapService.isPostpaid(DEFAULT_NUMERO);

        Assert.assertTrue(result);
    }

    @Test
    public void isNotPostpaid() {
        SouscriptionDto souscriptionDto= new SouscriptionDto();
        souscriptionDto.setProfil(ProfilType.PREPAID.name());

        ResponseEntity<SouscriptionDto> response = new ResponseEntity<>(souscriptionDto,HttpStatus.OK);

        when(serviceSOAP.getSouscription(any())).thenReturn(response);


        Boolean result = soapService.isPostpaid(DEFAULT_NUMERO);

        Assert.assertFalse(result);
    }

    @Test
    public void getAbonne() {

        CodeOTPCheckDTO codeOTPCheckDTO = new CodeOTPCheckDTO();

        codeOTPCheckDTO.setValid(true);
        codeOTPCheckDTO.setMsisdn(DEFAULT_NUMERO);
        codeOTPCheckDTO.setCode(DEFAULT_CODE);

        when(otpService.checkOPT(DEFAULT_NUMERO, DEFAULT_CODE)).thenReturn(codeOTPCheckDTO);


        ResponseEntity<AbonneDTO> response = ResponseEntity.status(HttpStatus.OK).build();

        when(serviceSOAP.getAbonne(any())).thenReturn(response);



       ResponseEntity<AbonneDTO> entity = soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE);
       Assert.assertEquals(HttpStatus.OK,entity.getStatusCode());

    }


    @Test
    public void getAbonneForBadRequestResponse() {

        CodeOTPCheckDTO codeOTPCheckDTO = new CodeOTPCheckDTO();

        codeOTPCheckDTO.setValid(false);
        codeOTPCheckDTO.setMsisdn(DEFAULT_NUMERO);
        codeOTPCheckDTO.setCode(DEFAULT_CODE);

        when(otpService.checkOPT(DEFAULT_NUMERO, DEFAULT_CODE)).thenReturn(codeOTPCheckDTO);


        ResponseEntity<AbonneDTO> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        ResponseEntity<AbonneDTO> entity = soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE);
        Assert.assertEquals(HttpStatus.BAD_REQUEST,entity.getStatusCode());

    }


   @Test
    public void getAbonneBadRequest() {

        Map<String, Collection<String>> headers = new LinkedHashMap<>();
        Response response = Response.builder().status(400)
            .headers(headers)
            .build();

        ResponseEntity<AbonneDTO> build = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        //restTemplate = new RestTemplate(response);
       // soapService = new SelfcareSoapService(restTemplate, otpService);

    }

    @Test
    public void getAbonneServiceUnavailable() {

         ResponseEntity<AbonneDTO> entity = soapService.getAbonne(DEFAULT_NUMERO,DEFAULT_CODE);

         assertTrue(HttpStatus.SERVICE_UNAVAILABLE.equals(entity.getStatusCode()));
    }

}
