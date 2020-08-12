package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.LogUtil;

import javax.validation.constraints.NotNull;

public class SponsorDTO {

    private Long id;

    @NotNull
    private String msisdn;

    private String matricule;

    private String firstName;

    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return LogUtil.convertObjectToJsonResponse(this);
    }
}
