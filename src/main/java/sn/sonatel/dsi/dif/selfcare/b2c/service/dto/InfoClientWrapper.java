package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;


import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.ClientType;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.LogUtil;

public class InfoClientWrapper {

   private ClientType clientType = ClientType.NOT_FOUND;
   private IndividualInformation information = new IndividualInformation();
   private OrganizationInformation organization = new OrganizationInformation();

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public IndividualInformation getInformation() {
        return information;
    }

    public void setInformation(IndividualInformation information) {
        this.information = information;
    }

    public OrganizationInformation getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationInformation organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return LogUtil.convertObjectToJsonResponse(this);
    }
}
