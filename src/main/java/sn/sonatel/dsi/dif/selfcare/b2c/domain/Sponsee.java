package sn.sonatel.dsi.dif.selfcare.b2c.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.LogUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Sponsee.
 */
@Entity
@Table(name = "sponsee")
public class Sponsee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "msisdn", nullable = false)
    private String msisdn;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "effective", nullable = false)
    private Boolean effective = false;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @ManyToOne
    @JsonIgnoreProperties("sponsees")
    private AccountB2C accountB2C;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public Sponsee msisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getFirstName() {
        return firstName;
    }

    public Sponsee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Sponsee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean isEffective() {
        return effective;
    }

    public Sponsee effective(Boolean effective) {
        this.effective = effective;
        return this;
    }

    public void setEffective(Boolean effective) {
        this.effective = effective;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Sponsee createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Sponsee enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public AccountB2C getAccountB2C() {
        return accountB2C;
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
        if (!(o instanceof Sponsee)) {
            return false;
        }
        return id != null && id.equals(((Sponsee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return LogUtil.convertObjectToJsonResponse(this);
    }
}
