package sn.sonatel.dsi.dif.selfcare.b2c.domain;


import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.NumeroDTO;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A AccountB2C.
 */
@Entity
@Table(name = "account_b_2_c")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccountB2C extends NumeroDTO implements Serializable {

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ApiModelProperty(required = false)
    @Email
    @Size(min = 5, max = 254)
    @Column(name = "email")
    private String email;

    @Column(name = "image_profil")
    private String imageProfil;

    @Size(max = 20)
    private String activationKey;

    @Size(min = 2, max = 6)
    private String langKey;

    @Column(name = "attempts", nullable = false)
    private int attempts=0;

    @OneToMany(mappedBy = "accountB2C")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RattachementLigne> users = new HashSet<>();

    @OneToMany(mappedBy = "accountB2C", cascade = CascadeType.ALL)
    private Set<Sponsee> sponsees = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Column(name = "derniere_connnexion_date", nullable = true)
    private ZonedDateTime derniereConnnexionDate;

    @Column(name = "tuto_viewed")
    private boolean tutoViewed = false;

    @Column(name = "email_activated")
    private boolean emailActivated = false;

    @OneToMany(mappedBy = "accountB2C")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NotificationInformation> notificationInformations = new HashSet<>();

    public ZonedDateTime getDerniereConnnexionDate() {
        return derniereConnnexionDate;
    }

    public void setDerniereConnnexionDate(ZonedDateTime derniereConnnexionDate) {
        this.derniereConnnexionDate = derniereConnnexionDate;
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

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
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

    public boolean isTutoViewed() {
        return tutoViewed;
    }

    public void setTutoViewed(boolean tutoViewed) {
        this.tutoViewed = tutoViewed;
    }

    public boolean isEmailActivated() {
        return emailActivated;
    }

    public void setEmailActivated(boolean emailActivated) {
        this.emailActivated = emailActivated;
    }

    public void setUsers(Set<RattachementLigne> rattachementLignes) {
        this.users = rattachementLignes;
    }

    public Set<Sponsee> getSponsees() {
        return sponsees;
    }

    public void setSponsees(Set<Sponsee> sponsees) {
        this.sponsees = sponsees;
    }

    public Set<NotificationInformation> getNotificationInformations() {
        return notificationInformations;
    }

    public void setNotificationInformations(Set<NotificationInformation> notificationInformations) {
        this.notificationInformations = notificationInformations;
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
        return "{" +
            "\"firstName\":" + firstName + ',' +
            "\"lastName\":" + lastName + ',' +
            "\"email\":" + email + ',' +
            "\"imageProfil\":" + imageProfil + ',' +
            "\"activationKey\":" + activationKey + ',' +
            "\"langKey\":" + langKey + ',' +
            "\"attempts\":" + attempts + ',' +
            "\"derniereConnnexionDate\":" + derniereConnnexionDate + ',' +
            "\"tutoViewed\":" + tutoViewed + ',' +
            "\"emailActivated\":" + emailActivated + ',' +
            "\"numero\":" + numero +
            '}';
    }
}
