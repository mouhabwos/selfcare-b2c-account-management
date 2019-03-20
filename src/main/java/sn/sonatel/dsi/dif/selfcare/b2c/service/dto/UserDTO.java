package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

import javax.validation.constraints.*;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO extends Same {

	private Long id;

	@ApiModelProperty(required = true)
	@NotNull(message = "Le numero ne peut pas être vide")
	@NotBlank(message = "Le numéro ne doit pas être vide")
	@Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = "Le numéro doit un numéro orange valide")
	@Size(min = 9, max = 18, message = "La taille du numéro doit être de 9 chiffres")
	private String login;

	@Size(max = 256)
	private String imageUrl;

	private Set<String> authorities;

	private String imageprofil;

    public UserDTO login(String login) {
        this.login = login;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public Set<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}

	public String getImageprofil() {
		return imageprofil;
	}

	public void setImageprofil(String imageprofil) {
		this.imageprofil = imageprofil;
	}

	@Override
	public String toString() {
		return "UserDTO{" + "login='" + login + '\'' + ", firstName='" + firstName + '\''
				+ ", lastName='" + lastName + '\'' + ", email='" + email + '\''
				+ ", imageUrl='" + imageUrl + '\''
				+ ", langKey='" + langKey  + "}";
	}

}
