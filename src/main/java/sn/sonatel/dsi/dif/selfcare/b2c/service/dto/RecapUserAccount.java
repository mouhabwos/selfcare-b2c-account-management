package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RecapUserAccount {

    @NotNull(message = "Le prénom ne doit pas être vide")
    @ApiModelProperty(required = true)
    @Pattern(regexp = Constants.NAME_REGEX, message = "La saisie du prénom est incorrecte")
    @Size(max = 50)
    @Column(name = "first_name", nullable = false)
    public String firstName;

    @NotNull(message = "Le nom ne doit pas être vide")
    @ApiModelProperty(required = true)
    @Pattern(regexp = Constants.NAME_REGEX, message = "La saisie du nom est incorrecte")
    @Size(max = 50)
    @Column(name = "last_name", nullable = false)
    public String lastName;

    @ApiModelProperty(required = false)
    @Email(message = "L'email doit être une adresse email bien formée")
    @Size(min = 5, max = 254)
    @Column(name = "email")
    public String email;

    @Size(max = 20)
    public String activationKey;

    @Size(min = 2, max = 6)
    public String langKey;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }
}
