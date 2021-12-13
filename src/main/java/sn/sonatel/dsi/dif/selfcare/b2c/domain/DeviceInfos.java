package sn.sonatel.dsi.dif.selfcare.b2c.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.LogUtil;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A DeviceInfos.
 */
@Entity
@Table(name = "device_infos")
public class DeviceInfos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id")
    private String deviceId;

    @ManyToOne
    @JsonIgnoreProperties("deviceInfos")
    private AccountB2C accountB2C;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public DeviceInfos deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public AccountB2C getAccountB2C() {
        return accountB2C;
    }

    public void setAccountB2C(AccountB2C accountB2C) {
        this.accountB2C = accountB2C;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceInfos)) {
            return false;
        }
        return id != null && id.equals(((DeviceInfos) o).id);
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
