package sn.sonatel.dsi.dif.selfcare.b2c.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
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
public class AccountB2C implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    private String numero;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "image_prfil")
    private String imagePrfil;

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

    public String getFirstName() {
        return firstName;
    }

    public AccountB2C firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public AccountB2C lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public AccountB2C email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagePrfil() {
        return imagePrfil;
    }

    public AccountB2C imagePrfil(String imagePrfil) {
        this.imagePrfil = imagePrfil;
        return this;
    }

    public void setImagePrfil(String imagePrfil) {
        this.imagePrfil = imagePrfil;
    }

    public Set<RattachementLigne> getUsers() {
        return users;
    }

    public AccountB2C users(Set<RattachementLigne> rattachementLignes) {
        this.users = rattachementLignes;
        return this;
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
            ", imagePrfil='" + getImagePrfil() + "'" +
            "}";
    }
}
