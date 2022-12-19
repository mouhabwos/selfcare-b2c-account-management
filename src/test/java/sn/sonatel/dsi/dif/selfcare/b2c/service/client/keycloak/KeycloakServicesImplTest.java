package sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak;

import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sn.sonatel.dsi.dif.selfcare.b2c.IntegrationTest;
import sn.sonatel.dsi.dif.selfcare.b2c.config.ApplicationProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.service.SMSNotificationService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak.dto.UserCredentialDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.impl.LoginAttemptServiceImpl;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ResetPasswordVM;

@ContextConfiguration(classes = {KeycloakServicesImpl.class, ApplicationProperties.class,
    KeycloakConfig.class})
@IntegrationTest
@ExtendWith(SpringExtension.class)
class KeycloakServicesImplTest {

    @Autowired
    private KeycloakServices keycloakServicesImpl;


    //private KeycloakBuilder keycloakBuilder;

    /**
     * Method under test: {@link KeycloakServicesImpl#registerUserInKeycloack(ManagedUserVM)}
     */
    @Test
    void testRegisterUserInKeycloack() {

        keycloakServicesImpl.registerUserInKeycloack(new ManagedUserVM());
    }

    /**
     * Method under test: {@link KeycloakServicesImpl#getToken(UserCredentialDTO)}
     */
    @Test
    void testGetToken() {


        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setPassword("iloveyou");
        userCredentialDTO.setUsername("janedoe");
        keycloakServicesImpl.getToken(userCredentialDTO);
    }

    /**
     * Method under test: {@link KeycloakServicesImpl#resetPassword(ResetPasswordVM)}
     */
    @Test
    void testResetPassword() {


        ResetPasswordVM resetPasswordVM = new ResetPasswordVM();
        resetPasswordVM.setHmac("Hmac");
        resetPasswordVM.setLogin("Login");
        resetPasswordVM.setNewPassword("iloveyou");
        keycloakServicesImpl.resetPassword(resetPasswordVM);
    }
}

