package sn.sonatel.dsi.dif.selfcare.b2c.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import sn.sonatel.dsi.dif.selfcare.b2c.service.AccountB2CService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.ValidationHmacService;
import sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak.KeycloakServices;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.CodeOTPCheckDTO;
import sn.sonatel.dsi.dif.selfcare.b2c.service.servicescall.selfcareservice.SelfcareOTPService;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.BadRequestAlertException;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors.LigneAlreadyRattachedException;

@ContextConfiguration(classes = {AccountB2CSecurityService.class})
@ExtendWith(SpringExtension.class)
class AccountB2CSecurityServiceTest {
    @Autowired
    private AccountB2CSecurityService accountB2CSecurityService;

    @MockBean
    private AccountB2CService accountB2CService;

    @MockBean
    private KeycloakServices keycloakServices;

    @MockBean
    private SelfcareOTPService selfcareOTPService;

    @MockBean
    private ValidationHmacService validationHmacService;

    /**
     * Method under test: {@link AccountB2CSecurityService#loginWithOtpCode(String, String)}
     */
    @Test
    void testLoginWithOtpCodeShouldThrowLigneAlreadyRattachedException() {
        CodeOTPCheckDTO codeOTPCheckDTO = new CodeOTPCheckDTO();
        codeOTPCheckDTO.setCode("Code");
        codeOTPCheckDTO.setMsisdn("Msisdn");
        codeOTPCheckDTO.setValid(true);
        when(this.selfcareOTPService.checkOPT((String) any(), (String) any())).thenReturn(codeOTPCheckDTO);
        when(this.accountB2CService.isLinkedAccount((String) any())).thenReturn(true);
        when(this.accountB2CService.isPrincipalAccount((String) any())).thenReturn(true);
        assertThrows(LigneAlreadyRattachedException.class,
            () -> this.accountB2CSecurityService.loginWithOtpCode("Login", "iloveyou"));
        verify(this.selfcareOTPService).checkOPT((String) any(), (String) any());
        verify(this.accountB2CService).isLinkedAccount((String) any());
    }

    /**
     * Method under test: {@link AccountB2CSecurityService#loginWithOtpCode(String, String)}
     */
    @Test
    void testLoginWithOtpCodeShouldThrowHttpClientErrorException() {
        CodeOTPCheckDTO codeOTPCheckDTO = new CodeOTPCheckDTO();
        codeOTPCheckDTO.setCode("Code");
        codeOTPCheckDTO.setMsisdn("Msisdn");
        codeOTPCheckDTO.setValid(true);
        when(this.selfcareOTPService.checkOPT((String) any(), (String) any())).thenReturn(codeOTPCheckDTO);
        when(this.accountB2CService.isLinkedAccount((String) any()))
            .thenThrow(new HttpClientErrorException(HttpStatus.CONTINUE));
        when(this.accountB2CService.isPrincipalAccount((String) any()))
            .thenThrow(new HttpClientErrorException(HttpStatus.CONTINUE));
        assertThrows(HttpClientErrorException.class,
            () -> this.accountB2CSecurityService.loginWithOtpCode("Login", "iloveyou"));
        verify(this.selfcareOTPService).checkOPT((String) any(), (String) any());
        verify(this.accountB2CService).isLinkedAccount((String) any());
    }

    /**
     * Method under test: {@link AccountB2CSecurityService#loginWithOtpCode(String, String)}
     */
    @Test
    void testLoginWithOtpCodeShouldThrowBadRequestAlertException() {
        CodeOTPCheckDTO codeOTPCheckDTO = mock(CodeOTPCheckDTO.class);
        when(codeOTPCheckDTO.isValid()).thenReturn(false);
        doNothing().when(codeOTPCheckDTO).setCode((String) any());
        doNothing().when(codeOTPCheckDTO).setMsisdn((String) any());
        doNothing().when(codeOTPCheckDTO).setValid(anyBoolean());
        codeOTPCheckDTO.setCode("Code");
        codeOTPCheckDTO.setMsisdn("Msisdn");
        codeOTPCheckDTO.setValid(true);
        when(this.selfcareOTPService.checkOPT((String) any(), (String) any())).thenReturn(codeOTPCheckDTO);
        when(this.accountB2CService.isLinkedAccount((String) any())).thenReturn(true);
        when(this.accountB2CService.isPrincipalAccount((String) any())).thenReturn(true);
        assertThrows(BadRequestAlertException.class,
            () -> this.accountB2CSecurityService.loginWithOtpCode("Login", "iloveyou"));
        verify(this.selfcareOTPService).checkOPT((String) any(), (String) any());
        verify(codeOTPCheckDTO).isValid();
        verify(codeOTPCheckDTO).setCode((String) any());
        verify(codeOTPCheckDTO).setMsisdn((String) any());
        verify(codeOTPCheckDTO).setValid(anyBoolean());
    }

    /**
     * Method under test: {@link AccountB2CSecurityService#loginWithOtpCode(String, String)}
     */
    @Test
    void testLoginWithOtpCodeWithPrincipalAccount() {

        CodeOTPCheckDTO codeOTPCheckDTO = mock(CodeOTPCheckDTO.class);
        when(codeOTPCheckDTO.isValid()).thenReturn(true);
        when(keycloakServices.resetPassword(any(), any())).thenReturn(ResponseEntity.accepted().build());
        when(keycloakServices.getToken(any())).thenReturn(ResponseEntity.ok(new AccessTokenResponse()));
        doNothing().when(codeOTPCheckDTO).setCode((String) any());
        doNothing().when(codeOTPCheckDTO).setMsisdn((String) any());
        doNothing().when(codeOTPCheckDTO).setValid(anyBoolean());
        codeOTPCheckDTO.setCode("Code");
        codeOTPCheckDTO.setMsisdn("777777777");
        codeOTPCheckDTO.setValid(true);
        when(this.selfcareOTPService.checkOPT((String) any(), (String) any())).thenReturn(codeOTPCheckDTO);
        when(this.accountB2CService.isLinkedAccount((String) any())).thenReturn(false);
        when(this.accountB2CService.isPrincipalAccount((String) any())).thenReturn(true);
        this.accountB2CSecurityService.loginWithOtpCode("777777777", "iloveyou");
    }

}

