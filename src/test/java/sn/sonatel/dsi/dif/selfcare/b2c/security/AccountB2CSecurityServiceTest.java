package sn.sonatel.dsi.dif.selfcare.b2c.security;


import io.github.jhipster.config.JHipsterProperties;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;

import static org.mockito.MockitoAnnotations.initMocks;

public class AccountB2CSecurityServiceTest {

    private AccountB2CSecurityService accountB2cSecurityService;

    private  JHipsterProperties jHipsterProperties;
    @Mock
    private  RestTemplate restTemplate;
    @Mock
    private  AccountB2CService accountB2CService;

    @Before
    public void setUp() {
        initMocks(this);
        jHipsterProperties= new JHipsterProperties();
        jHipsterProperties.getSecurity().getClientAuthorization().setAccessTokenUri("http://selfcare-uaa/oauth/token");
        accountB2cSecurityService = new AccountB2CSecurityService(jHipsterProperties,restTemplate,accountB2CService);
    }

    @Test
    public void registerAccountB2CV3(){
        ManagedUserVM managedUserVM =  new ManagedUserVM();
        managedUserVM.setPassword("password10");
        managedUserVM.setLogin("782900000");

        Mockito.when(accountB2CService.register(Mockito.any())).thenReturn(new AccountB2C());
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(ResponseEntity.ok(new DefaultOAuth2AccessToken("token")));

        OAuth2AccessToken oAuth2AccessToken = accountB2cSecurityService.registerAccountB2CV3(managedUserVM);

        Assertions.assertThat(oAuth2AccessToken.getValue()).isEqualTo("token");
    }

    @Test(expected= HttpClientErrorException.class)
    public void registerAccountB2CV3ShouldThrowHttpClientErrorException(){
        ManagedUserVM managedUserVM =  new ManagedUserVM();
        managedUserVM.setPassword("password10");
        managedUserVM.setLogin("782900000");

        Mockito.when(accountB2CService.register(Mockito.any())).thenReturn(new AccountB2C());
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(ResponseEntity.notFound().build());

         accountB2cSecurityService.registerAccountB2CV3(managedUserVM);

    }
    @Test(expected= InvalidClientException.class)
    public void registerAccountB2CV3ShouldThrowInvalidClientException(){

        jHipsterProperties.getSecurity().getClientAuthorization().setAccessTokenUri(null);

        ManagedUserVM managedUserVM =  new ManagedUserVM();
        managedUserVM.setPassword("password10");
        managedUserVM.setLogin("782900000");

        Mockito.when(accountB2CService.register(Mockito.any())).thenReturn(new AccountB2C());
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(ResponseEntity.notFound().build());

         accountB2cSecurityService.registerAccountB2CV3(managedUserVM);

    }
}
