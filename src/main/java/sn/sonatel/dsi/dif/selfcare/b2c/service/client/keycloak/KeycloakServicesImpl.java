package sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak.dto.UserCredentialDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ManagedUserVM;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm.ResetPasswordVM;

@Service
class KeycloakServicesImpl implements KeycloakServices {

    private final Logger log = LoggerFactory.getLogger(KeycloakServicesImpl.class);
    @Value("${keycloak-configuration.realm}")
    private String realmName;
    private final KeycloakBuilder keycloakBuilder;

    KeycloakServicesImpl(KeycloakBuilder keycloakBuilder) {
        this.keycloakBuilder = keycloakBuilder;
    }

    @Override
    public ResponseEntity registerUserInKeycloack(ManagedUserVM userVM) {
        Keycloak keycloak = keycloakBuilder.grantType(OAuth2Constants.CLIENT_CREDENTIALS).build();
        return registerUserInKeycloack(userVM, keycloak);
    }

    private ResponseEntity<Void> registerUserInKeycloack(ManagedUserVM userVM, Keycloak instance) {
        // Define user
        UserRepresentation user = new UserRepresentation();
        List<String> groups = new ArrayList<>();
        groups.add("/Users/ABONNE");
        user.setEnabled(true);
        user.setUsername(userVM.getLogin());
        user.setFirstName(userVM.getFirstName());
        user.setLastName(userVM.getLastName());
        user.setEmail(userVM.getEmail());
        user.setGroups(groups);
        user.setAttributes(Collections.emptyMap());

        // Get realm
        UsersResource usersResource = instance.realm(realmName).users();

        Response response = usersResource.create(user);
        if (response.getStatus() == HttpStatus.SC_CREATED) {
            String userId = CreatedResponseUtil.getCreatedId(response);
            // Define password credential
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(userVM.getPassword());
            UserResource userResource = usersResource.get(userId);

            // Set password credential
            userResource.resetPassword(passwordCred);

            return ResponseEntity.status(HttpStatus.SC_CREATED).build();
        }

        return ResponseEntity.status(response.getStatus()).build();
    }

    @Override
    public ResponseEntity<AccessTokenResponse> getToken(UserCredentialDTO userCredential) {
        try {
            var keycloak = keycloakBuilder
                .grantType(OAuth2Constants.PASSWORD)
                .username(userCredential.getUsername())
                .password(userCredential.getPassword())
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

            return ResponseEntity.ok(keycloak.tokenManager().getAccessToken());
        } catch (NotAuthorizedException exception) {
            log.error("Error when getting token", exception);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity resetPassword(ResetPasswordVM resetPassword) {
        Keycloak keycloak = keycloakBuilder.grantType(OAuth2Constants.CLIENT_CREDENTIALS).build();

        UserRepresentation userRepresentation = getUserRepresentation(resetPassword.getLogin(), keycloak);
        if (userRepresentation == null) {
            ManagedUserVM userVM = new ManagedUserVM();
            userVM.setLogin(resetPassword.getLogin());
            userVM.setPassword(resetPassword.getNewPassword());
            return registerUserInKeycloack(userVM, keycloak);
        }

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(resetPassword.getNewPassword());

        //Get UserResource
        UserResource userResource = keycloak.realm(realmName).users().get(userRepresentation.getId());

        // Set password credential
        userResource.resetPassword(passwordCred);

        return ResponseEntity.accepted().build();
    }

    private UserRepresentation getUserRepresentation(String login, Keycloak keycloak) {
        RealmResource realmResource = keycloak.realm(realmName);
        UsersResource usersRessource = realmResource.users();
        List<UserRepresentation> userRepresentationList = usersRessource.search(login);

        return (!userRepresentationList.isEmpty()) ? userRepresentationList.get(0) : null;
    }

}
