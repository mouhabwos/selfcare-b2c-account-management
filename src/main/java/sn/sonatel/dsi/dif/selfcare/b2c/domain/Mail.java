package sn.sonatel.dsi.dif.selfcare.b2c.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.StatusMail;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.LogUtil;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 */

@Entity
@Table(name = "mail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Mail implements Serializable {

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "id_request", nullable = false)
    private String idRequest = "";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private Long id;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "status", nullable = false)
    private StatusMail status = StatusMail.IN_PROGRESS;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "operation_code", nullable = false)
    private String operationCode = "";

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "operation_titre", nullable = false)
    private String operationTitre = "";

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email = "";

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "id_formulaire", nullable = false)
    private String idFormulaire = "";

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "id_recto", nullable = false)
    private String idRecto = "";

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "id_verso", nullable = false)
    private String idVerso = "";

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "numero", nullable = false)
    private String numero;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "firsname", nullable = false)
    private String firsName;

    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    @NotNull
    @Column(name = "lastname", nullable = false)
    private  String lastName;

    @Override
    public String toString() {
        return LogUtil.convertObjectToJsonResponse(this);
    }
}
