package sn.sonatel.dsi.dif.selfcare.b2c.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.TypeNumero;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NumeroDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RattachementLigne.
 */
@Entity
@Table(name = "rattachement_ligne")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RattachementLigne extends NumeroDTO implements Serializable {

    @NotNull(message = "Le type de numéro ne peut pas être vide")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_numero", nullable = false)
    private TypeNumero typeNumero;

    @ManyToOne
    @JsonIgnoreProperties("users")
    private AccountB2C accountB2C;

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
            ", typeNumero='" + getTypeNumero() + "'" +
            "}";
    }
}
