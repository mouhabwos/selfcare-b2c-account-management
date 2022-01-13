package sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak.dto.UserCredentialDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ResetPasswordVM;

public interface KeycloakServices {

    ResponseEntity<String> registerUserInKeycloack(ManagedUserVM userVM);

    ResponseEntity<AccessTokenResponse> getToken(UserCredentialDTO userCredential);

    ResponseEntity resetPassword(ResetPasswordVM resetPasswordVM, String uuid);

    ResponseEntity activateAccountUserInKeycloack(String username);

    ResponseEntity deactivateAccountUserInKeycloack(String username);
}
