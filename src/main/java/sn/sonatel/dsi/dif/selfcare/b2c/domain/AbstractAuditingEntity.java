package sn.sonatel.dsi.dif.selfcare.b2c.domain;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.UserBaseClass;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity extends UserBaseClass implements Serializable {

    private static final long serialVersionUID = 1L;


}
