package sn.sonatel.dsi.dif.selfcare.b2c.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RecapUserAccount;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AccountB2C.
 */
@Entity
@Table(name = "account_b_2_c")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "accountb2c")
public class AccountB2C extends RecapUserAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ApiModelProperty(required = true)
    @NotNull(message = "Le numero ne peut pas être vide")
    @NotBlank(message = "Le numéro ne doit pas être vide")
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = "Le numéro doit un numéro orange valide")
    @Size(min = 9, max = 18, message = "La taille du numéro doit être de 9 chiffres")
    @Column(name = "numero", nullable = false)
    private String numero;



    @Column(name = "image_profil")
    private String imageProfil;

    private int attempts=0;

    @OneToMany(mappedBy = "accountB2C")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RattachementLigne> users = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public AccountB2C numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }


    public String getImageProfil() {
        return imageProfil;
    }

    public AccountB2C imageProfil(String imageProfil) {
        this.imageProfil = imageProfil;
        return this;
    }

    public void setImageProfil(String imageProfil) {
        this.imageProfil = imageProfil;
    }

    public Set<RattachementLigne> getUsers() {
        return users;
    }

    public AccountB2C users(Set<RattachementLigne> rattachementLignes) {
        this.users = rattachementLignes;
        return this;
    }


    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public AccountB2C addUser(RattachementLigne rattachementLigne) {
        this.users.add(rattachementLigne);
        rattachementLigne.setAccountB2C(this);
        return this;
    }

    public AccountB2C removeUser(RattachementLigne rattachementLigne) {
        this.users.remove(rattachementLigne);
        rattachementLigne.setAccountB2C(null);
        return this;
    }

    public void setUsers(Set<RattachementLigne> rattachementLignes) {
        this.users = rattachementLignes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountB2C accountB2C = (AccountB2C) o;
        if (accountB2C.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountB2C.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountB2C{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", imagePrfil='" + getImageProfil() + "'" +
            "}";
    }
}
