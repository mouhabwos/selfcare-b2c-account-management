package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RattachementLigneVM {

    @ApiModelProperty(required = true)
    @NotNull(message = "Le numero ne peut pas être vide")
    @NotBlank(message = "Le numéro ne doit pas être vide")
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = "Le numéro doit un numéro orange valide")
    @Size(min = 9, max = 18, message = "La taille du numéro doit être de 9 chiffres")
    private String numero;

    private String typeVerification;

    private String codeVerification;

    private Boolean statut;

    private String login;

    @NotNull(message = "Le type de numéro ne peut pas être vide")
    @Enumerated(EnumType.STRING)
    private TypeNumero typeNumero;

    public RattachementLigneVM() {
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
