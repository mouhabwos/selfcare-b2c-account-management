package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.RattachementLigne;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

public class AccountB2CDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ApiModelProperty(required = true)
    @NotNull(message = "Le numero ne peut pas être vide")
    @NotBlank(message = "Le numéro ne doit pas être vide")
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = "Le numéro doit un numéro orange valide")
    @Column(name = "numero", nullable = false)
    private String numero;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "image_prfil")
    private String imagePrfil;

    @OneToMany(mappedBy = "accountB2C")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RattachementLigne> users = new HashSet<>();

    public AccountB2CDTO() {

        //Default Constructor

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public String getImagePrfil() {
        return imagePrfil;
    }

    public void setImagePrfil(String imagePrfil) {
        this.imagePrfil = imagePrfil;
    }

    public Set<RattachementLigne> getUsers() {
        return users;
    }

    public void setUsers(Set<RattachementLigne> users) {
        this.users = users;
    }
}
