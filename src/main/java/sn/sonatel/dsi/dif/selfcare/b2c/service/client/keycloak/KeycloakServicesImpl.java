package sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak;

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

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
 class KeycloakServicesImpl implements KeycloakServices{

    private final Logger log = LoggerFactory.getLogger(KeycloakServicesImpl.class);

    @Value("${keycloak-configutation.serverUrl}")
    private String serverUrl;

    @Value("${keycloak-configutation.realm}")
    private String realmName;

    @Value("${keycloak-configutation.client-id}")
    private String clientId;

    @Value("${keycloak-configutation.client-secret}")
    private String clientSecret;

    @Override
    public ResponseEntity registerUserInKeycloack( ManagedUserVM userVM){

        Keycloak keycloak = getIntanceKeycloak();

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
        RealmResource realmResource = keycloak.realm(realmName);
        UsersResource usersRessource = realmResource.users();
        Response response = usersRessource.create(user);
        if(response.getStatus()== HttpStatus.SC_CREATED){

            String userId = CreatedResponseUtil.getCreatedId(response);
            // Define password credential
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(userVM.getPassword());
            UserResource userResource = usersRessource.get(userId);

            // Set password credential
            userResource.resetPassword(passwordCred);

            return ResponseEntity.status(HttpStatus.SC_CREATED).build();
        }

        return ResponseEntity.status(response.getStatus()).build();

    }

    @Override
    public ResponseEntity<AccessTokenResponse> getToken(UserCredentialDTO userCredential) {

        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .grantType(OAuth2Constants.PASSWORD)
                .realm(realmName)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(userCredential.getUsername())
                .password(userCredential.getPassword())
                .resteasyClient(
                    new ResteasyClientBuilder()
                        .connectionPoolSize(10).build()
                ).build();

            return ResponseEntity.ok(keycloak.tokenManager().getAccessToken());
        }catch (NotAuthorizedException exception){
            log.error("Error when getting token", exception);
            return ResponseEntity.badRequest().build();
        }

    }

    @Override
    public ResponseEntity resetPassword(ResetPasswordVM resetPassword, String uuid) {


        try {
            Keycloak keycloak = getIntanceKeycloak();
            // Get realm
            RealmResource realmResource = keycloak.realm(realmName);
            UsersResource usersRessource = realmResource.users();

            UserRepresentation userRepresentations = keycloak.realm(realmName).users().search(resetPassword.getLogin()).get(0);

            // Define password credential
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            passwordCred.setTemporary(false);
            passwordCred.setType(CredentialRepresentation.PASSWORD);
            passwordCred.setValue(resetPassword.getNewPassword());
            UserResource userResource = usersRessource.get(userRepresentations.getId());

            // Set password credential
            userResource.resetPassword(passwordCred);

            return ResponseEntity.accepted().build();
        }catch (Exception ex){

        }
        return ResponseEntity.internalServerError().build();
    }

    private Keycloak getIntanceKeycloak(){
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realmName)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();
    }
}
