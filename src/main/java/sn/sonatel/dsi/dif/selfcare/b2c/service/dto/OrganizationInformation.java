package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import java.util.HashSet;
import java.util.Set;

public class OrganizationInformation {

    private String id;

    private String href;

    private String isLegalEntity;

    private String type;

    private String tradingName;

    private String nameType;

    private String status;

    private Set<OrganizationIdentification> organizationIdentification = new HashSet<>();

    public OrganizationInformation() {
        // Default constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIsLegalEntity() {
        return isLegalEntity;
    }

    public void setIsLegalEntity(String isLegalEntity) {
        this.isLegalEntity = isLegalEntity;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<OrganizationIdentification> getOrganizationIdentification() {
        return organizationIdentification;
    }

    public void setOrganizationIdentification(Set<OrganizationIdentification> organizationIdentification) {
        this.organizationIdentification = organizationIdentification;
    }
}
