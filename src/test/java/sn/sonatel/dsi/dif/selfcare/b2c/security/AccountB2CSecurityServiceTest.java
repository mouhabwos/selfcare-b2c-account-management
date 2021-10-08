package sn.sonatel.dsi.dif.selfcare.b2c.security;


import io.github.jhipster.config.JHipsterProperties;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.security.oauth2.OAuth2TokenEndpointClient;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.ValidationHmacService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareUAAService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ResetPasswordVM;

import static org.mockito.MockitoAnnotations.initMocks;

public class AccountB2CSecurityServiceTest {

   private static final String MSISDN = "782363572";
   private static final String HMAC = "0bb076c084c277c77c385bcb13559ca2e5f353201483c28f64dcc7cc57797968";
   private static final String UUID = "7899";

    private AccountB2CSecurityService accountB2cSecurityService;

    private  JHipsterProperties jHipsterProperties;
    @Mock
    private  RestTemplate restTemplate;
    @Mock
    private  AccountB2CService accountB2CService;
    @Mock
    private ValidationHmacService validationHmacService;
    @Mock
    private OAuth2TokenEndpointClient authorizationClient;

    @Mock
    private SelfcareUAAService uaaService;

    @Before
    public void setUp() {
        initMocks(this);
        jHipsterProperties= new JHipsterProperties();
        jHipsterProperties.getSecurity().getClientAuthorization().setAccessTokenUri("http://selfcare-uaa/oauth/token");
        accountB2cSecurityService = new AccountB2CSecurityService(jHipsterProperties,restTemplate,accountB2CService, validationHmacService, authorizationClient, uaaService);
    }

    @Test
    public void registerAccountB2CV3(){
        ManagedUserVM managedUserVM =  new ManagedUserVM();
        managedUserVM.setPassword("password10");
        managedUserVM.setLogin("782900000");

        Mockito.when(accountB2CService.register(managedUserVM, true)).thenReturn(new AccountB2C());
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(ResponseEntity.ok(new DefaultOAuth2AccessToken("token")));
        Mockito.when(authorizationClient.sendPasswordGrant(Mockito.anyString(), Mockito.anyString())).thenReturn(new DefaultOAuth2AccessToken("token"));

        OAuth2AccessToken oAuth2AccessToken = accountB2cSecurityService.registerAccountB2CV3(managedUserVM);

        Assertions.assertThat(oAuth2AccessToken.getValue()).isEqualTo("token");
    }

    @Test(expected= HttpClientErrorException.class)
    public void registerAccountB2CV3ShouldThrowHttpClientErrorException(){
        ManagedUserVM managedUserVM =  new ManagedUserVM();
        managedUserVM.setPassword("password10");
        managedUserVM.setLogin("782900000");

        Mockito.when(accountB2CService.register(managedUserVM, true)).thenReturn(new AccountB2C());
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(ResponseEntity.notFound().build());
        Mockito.when(authorizationClient.sendPasswordGrant(Mockito.anyString(), Mockito.anyString())).thenThrow( new HttpClientErrorException(HttpStatus.FORBIDDEN));


        accountB2cSecurityService.registerAccountB2CV3(managedUserVM);

    }

    @Test
    public void resetPasswordSuccess(){
        ResetPasswordVM passwordVM = new ResetPasswordVM();
        passwordVM.setLogin(MSISDN);
        passwordVM.setHmac(HMAC);

        Mockito.when(authorizationClient.sendPasswordGrant(Mockito.anyString(), Mockito.anyString())).thenReturn(new DefaultOAuth2AccessToken("access-token"));
        Mockito.when(uaaService.resetPassword(Mockito.any(), Mockito.any())).thenReturn(ResponseEntity.ok().build());
        OAuth2AccessToken oAuth2AccessToken = accountB2cSecurityService.resetPassword(UUID, passwordVM);
        Assertions.assertThat(oAuth2AccessToken.getValue()).isEqualTo("access-token");
    }

    @Test(expected= HttpClientErrorException.class)
    public void resetPasswordFaild(){
        ResetPasswordVM passwordVM = new ResetPasswordVM();
        passwordVM.setLogin(MSISDN);
        passwordVM.setHmac(HMAC);

        Mockito.when(uaaService.resetPassword(Mockito.any(), Mockito.any())).thenReturn(ResponseEntity.badRequest().build());
        accountB2cSecurityService.resetPassword(UUID, passwordVM);
    }
}
