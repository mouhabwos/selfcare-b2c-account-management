package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.Constants;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RattachementBaseClass {

    @ApiModelProperty(required = true)
    @NotNull(message = MessageValidation.NUMERO_NON_VIDE)
    @NotBlank(message = MessageValidation.NUMERO_NON_VIDE)
    @Pattern(regexp = Constants.VALIDE_NUMBER_ORANGE_FIXE_MOBILE, message = MessageValidation.NUMERO_ORANGE_VALIDE)
    @Size(min = 9, max = 18, message = MessageValidation.NUMERO_TAILLE_VALIDE)
    private String numero;

    private String login;

    @NotNull(message = MessageValidation.TYPE_NUMERO_NON_VIDE)
    @Enumerated(EnumType.STRING)
    private TypeNumero typeNumero;

    public RattachementBaseClass numero(String numero) {
        this.numero = numero;
        return this;
    }



    public RattachementBaseClass typeNumero(TypeNumero typeNumero) {
        this.typeNumero = typeNumero;
        return this;
    }


    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    @Override
    public String toString() {
        return "{" +
            "\"numero\":" + numero + ',' +
            "\"login\":" + login + ',' +
            "\"typeNumero\":" + typeNumero +
            '}';
    }
}

