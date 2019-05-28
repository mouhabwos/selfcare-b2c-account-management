package sn.sonatel.dsi.dif.selfcare.b2c.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import sn.sonatel.dsi.dif.selfcare.b2c.SelfcareB2CApp;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.config.SecurityBeanOverrideConfiguration;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.AccountB2C;
import sn.sonatel.dsi.dif.selfcare.b2c.exception.AccountB2CException;
import sn.sonatel.dsi.dif.selfcare.b2c.repository.AccountB2CRepository;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareOTPService;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SelfcareB2CApp.class})
public class LoginAttemptServiceImplTest {

    private static final String DEFAULT_USERNAME = "781320607";
    private static final String DEFAULT_USERNAME2 = "781300101";

    private LoginAttemptServiceImpl loginAttemptService;

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private AccountB2CRepository b2CRepository;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private SelfcareOTPService serviceOTP;

    private void createEntity4() {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero(DEFAULT_USERNAME);
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test07@gmail.com");
        accountB2C.setImageProfil("image");
        b2CRepository.save(accountB2C);

    }

    private void createEntity2() {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero(DEFAULT_USERNAME2);
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test70@gmail.com");
        accountB2C.setImageProfil("image");
        accountB2C.setDerniereConnnexionDate(ZonedDateTime.now());
        accountB2C.setAttempts(0);
        b2CRepository.save(accountB2C);

    }

    private void createEntity3() {
        AccountB2C accountB2C = new AccountB2C();
        accountB2C.setNumero("789009090");
        accountB2C.setFirstName("test");
        accountB2C.setLastName("test");
        accountB2C.setEmail("test90@gmail.com");
        accountB2C.setImageProfil("image");
        accountB2C.setDerniereConnnexionDate(ZonedDateTime.now());
        accountB2C.setAttempts(0);
        b2CRepository.save(accountB2C);

    }


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        loginAttemptService = new LoginAttemptServiceImpl(restTemplate, b2CRepository, applicationProperties, serviceOTP);

    }

    @Test
    public void loginSucceeded() throws AccountB2CException {

        createEntity2();
        loginAttemptService.loginSucceeded(DEFAULT_USERNAME2);
    }

    @Test
    public void loginFailedIsBlocked() throws AccountB2CException {
    createEntity4();
        for (int i=0; i<=3; i++){

            loginAttemptService.loginFailed(DEFAULT_USERNAME);

        }

    }

    @Test
    public void isBlocked() {
        createEntity3();
        boolean  check = loginAttemptService.isBlocked("789009090");

        assertThat(check).isEqualTo(true);

    }
}
