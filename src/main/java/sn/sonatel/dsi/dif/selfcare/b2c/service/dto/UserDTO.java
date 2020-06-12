package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.Scope;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.LogUtil;

import javax.validation.constraints.*;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO extends UserBaseClass {

	private Long id;

	@ApiModelProperty(required = true)
	@NotNull(message = MessageValidation.NUMERO_NON_VIDE)
	@NotBlank(message = MessageValidation.NUMERO_NON_VIDE)
	@Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = MessageValidation.NUMERO_ORANGE_VALIDE)
	@Size(min = 9, max = 18, message = MessageValidation.NUMERO_TAILLE_VALIDE)
	private String login;

    @NotNull(message = MessageValidation.FIRSTNAME_NON_VIDE)
    @ApiModelProperty(required = true)
    @Size(max = 50)
    private String firstName;

    @NotNull(message = MessageValidation.LASTNAME_NON_VIDE)
    @ApiModelProperty(required = true)
    @Size(max = 50)
    private String lastName;

    @ApiModelProperty(required = false)
    @Email(message = MessageValidation.EMAIL_VALID)
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 20)
    private String activationKey;

    @Size(min = 2, max = 6)
    private String langKey;


	@Size(max = 256)
	private String imageUrl;

	private boolean activated = false;

	private Set<String> authorities;

	private String imageprofil;

    private Scope scope = Scope.B2C;

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


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
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

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String toString() {
		return LogUtil.convertObjectToJsonResponse(this);
	}


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


    public String getActivationKey() {
        return activationKey;
    }


    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }
}
