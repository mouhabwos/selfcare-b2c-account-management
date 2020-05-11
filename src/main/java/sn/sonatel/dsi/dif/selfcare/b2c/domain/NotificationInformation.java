package sn.sonatel.dsi.dif.selfcare.b2c.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A InfoNotification.
 */
@Entity
@Table(name = "notification_information",
    indexes = {
        @Index(name = "NOTIFICATION_INFORMATION_INDX_0", columnList = "code_formule") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NotificationInformation implements Serializable {

    @Size(min = 4, max = 30)
    @Column(name = "code_formule")
    private String codeFormule;

    @Column(name = "firebase_id")
    private String firebaseId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JsonIgnoreProperties("notificationInformations")
    private AccountB2C accountB2C;

    public NotificationInformation() {
        //default constructor
    }

    public String getCodeFormule() {
        return codeFormule;
    }

    public void setCodeFormule(String codeFormule) {
        this.codeFormule = codeFormule;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccountB2C getAccountB2C() {
        return accountB2C;
    }

    public void setAccountB2C(AccountB2C accountB2C) {
        this.accountB2C = accountB2C;
    }

    @Override
    public String toString() {
        return "InfoNotification{" +
            "codeFormule='" + codeFormule + '\'' +
            ", firebaseId='" + firebaseId + '\'' +
            ", id=" + id +
            '}';
    }
}
