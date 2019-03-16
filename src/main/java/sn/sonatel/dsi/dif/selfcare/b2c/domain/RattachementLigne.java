package sn.sonatel.dsi.dif.selfcare.b2c.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;

/**
 * A RattachementLigne.
 */
@Entity
@Table(name = "rattachement_ligne")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "rattachementligne")
public class RattachementLigne implements Serializable {

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

    @Column(name = "type_verification")
    private String typeVerification;

    @Column(name = "code_verification")
    private String codeVerification;

    @Column(name = "statut")
    private Boolean statut;

    @NotNull(message = "Le type de numéro ne peut pas être vide")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_numero", nullable = false)
    private TypeNumero typeNumero;

    @ManyToOne
    @JsonIgnoreProperties("users")
    private AccountB2C accountB2C;

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

    public RattachementLigne numero(String numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTypeVerification() {
        return typeVerification;
    }

    public RattachementLigne typeVerification(String typeVerification) {
        this.typeVerification = typeVerification;
        return this;
    }

    public void setTypeVerification(String typeVerification) {
        this.typeVerification = typeVerification;
    }

    public String getCodeVerification() {
        return codeVerification;
    }

    public RattachementLigne codeVerification(String codeVerification) {
        this.codeVerification = codeVerification;
        return this;
    }

    public void setCodeVerification(String codeVerification) {
        this.codeVerification = codeVerification;
    }

    public Boolean isStatut() {
        return statut;
    }

    public RattachementLigne statut(Boolean statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }


    public TypeNumero getTypeNumero() {
        return typeNumero;
    }

    public RattachementLigne typeNumero(TypeNumero typeNumero) {
        this.typeNumero = typeNumero;
        return this;
    }

    public void setTypeNumero(TypeNumero typeNumero) {
        this.typeNumero = typeNumero;
    }

    public AccountB2C getAccountB2C() {
        return accountB2C;
    }

    public RattachementLigne accountB2C(AccountB2C accountB2C) {
        this.accountB2C = accountB2C;
        return this;
    }

    public void setAccountB2C(AccountB2C accountB2C) {
        this.accountB2C = accountB2C;
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
        RattachementLigne rattachementLigne = (RattachementLigne) o;
        if (rattachementLigne.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rattachementLigne.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RattachementLigne{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", typeVerification='" + getTypeVerification() + "'" +
            ", codeVerification='" + getCodeVerification() + "'" +
            ", statut='" + isStatut() + "'" +
            ", typeNumero='" + getTypeNumero() + "'" +
            "}";
    }
}
