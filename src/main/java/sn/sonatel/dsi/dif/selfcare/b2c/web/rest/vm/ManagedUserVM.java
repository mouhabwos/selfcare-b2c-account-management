package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

	public static final int PASSWORD_MIN_LENGTH = 4;

	public static final int PASSWORD_MAX_LENGTH = 100;

	@NotNull(message = "Le mot de passe ne doit pas être vide")
	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = "Le password doit contenir au moins 8 caracteres")
	@Pattern(regexp = Constants.MOTDEPASSE_REGEX, message = "Le mot de passe saisi est incorrect")
	@ApiModelProperty(required = true)
	private String password;

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

	@Override
	public String toString() {
		return "ManagedUserVM{" + "} " + super.toString();
	}

}
