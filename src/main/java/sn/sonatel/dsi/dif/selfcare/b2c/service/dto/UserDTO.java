package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

	private Long id;

	@ApiModelProperty(required = true)
	@NotNull(message = "Le numero ne peut pas être vide")
	@NotBlank(message = "Le numéro ne doit pas être vide")
	@Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = "Le numéro doit un numéro orange valide")
	@Size(min = 9, max = 18, message = "La taille du numéro doit être de 9 chiffres")
	private String login;

	@NotNull(message = "Le prénom ne doit pas être vide")
	@ApiModelProperty(required = true)
	@Pattern(regexp = Constants.NAME_REGEX, message = "La saisie du prénom est incorrecte")
	@Size(max = 50)
	private String firstName;

	@NotNull(message = "Le nom ne doit pas être vide")
	@ApiModelProperty(required = true)
	@Pattern(regexp = Constants.NAME_REGEX, message = "La saisie du nom est incorrecte")
	@Size(max = 50)
	private String lastName;

	@Email(message = "L'email doit être une adresse email bien formée")
	@Size(min = 5, max = 254)
	private String email;

	@Size(max = 256)
	private String imageUrl;

	private boolean activated = false;

	@Size(min = 2, max = 6)
	private String langKey;

	private String createdBy;

	private Instant createdDate;

	private String lastModifiedBy;

	private Instant lastModifiedDate;

	private Set<String> authorities;

	private String imageprofil;

	public UserDTO() {
		// Empty constructor needed for Jackson.
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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
				+ ", imageUrl='" + imageUrl + '\'' + ", activated=" + activated
				+ ", langKey='" + langKey + '\'' + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", lastModifiedBy='" + lastModifiedBy
				+ '\'' + ", lastModifiedDate=" + lastModifiedDate + ", authorities="
				+ authorities + "}";
	}

}
