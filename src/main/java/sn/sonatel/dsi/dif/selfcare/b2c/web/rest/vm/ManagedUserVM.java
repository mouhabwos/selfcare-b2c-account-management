package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.MessageValidation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;

    public static final int PASSWORD_MAX_LENGTH = 19;

    @NotBlank(message = MessageValidation.MOTE_DE_PASSE_NON_VIDE)
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = MessageValidation.MOTE_DE_PASSE_TAILLE_VALIDE)
    @ApiModelProperty(required = true)
    private String password;

    private String hmac;

    private String uuid;

    public ManagedUserVM(String password) {
        this.password = password;

    }

    public ManagedUserVM() {

        // Empty constructor needed for Jackson.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" + "} " + super.toString ();
    }

}
