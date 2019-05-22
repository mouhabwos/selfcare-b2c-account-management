package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RattachementBase {

    @ApiModelProperty(required = true)
    @NotNull(message = MessageValidation.NUMERO_NON_VIDE)
    @NotBlank(message = MessageValidation.NUMERO_NON_VIDE)
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = MessageValidation.NUMERO_ORANGE_VALIDE)
    @Size(min = 9, max = 18, message = MessageValidation.NUMERO_TAILLE_VALIDE)
    private String numero;

    private String typeVerification;

    private String codeVerification;

    private Boolean statut;

    private String login;

    @NotNull(message = MessageValidation.TYPE_NUMERO_NON_VIDE)
    @Enumerated(EnumType.STRING)
    private TypeNumero typeNumero;

    public RattachementBase numero(String numero) {
        this.numero = numero;
        return this;
    }

    public RattachementBase typeVerification(String typeVerification) {
        this.typeVerification = typeVerification;
        return this;
    }

    public RattachementBase codeVerification(String codeVerification) {
        this.codeVerification = codeVerification;
        return this;
    }

    public RattachementBase statut(boolean statut) {
        this.statut = statut;
        return this;
    }

    public RattachementBase typeNumero(TypeNumero typeNumero) {
        this.typeNumero = typeNumero;
        return this;
    }


    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTypeVerification() {
        return typeVerification;
    }

    public void setTypeVerification(String typeVerification) {
        this.typeVerification = typeVerification;
    }

    public String getCodeVerification() {
        return codeVerification;
    }

    public void setCodeVerification(String codeVerification) {
        this.codeVerification = codeVerification;
    }

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public TypeNumero getTypeNumero() {
        return typeNumero;
    }

    public void setTypeNumero(TypeNumero typeNumero) {
        this.typeNumero = typeNumero;
    }
}

