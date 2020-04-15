package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import javax.validation.constraints.Size;

public class NotificationInformationDTO {

    private String firebaseId;
    private String msisdn;

    @Size(min = 4)
    private String codeFormule;

    public NotificationInformationDTO() {
        //default constructor
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCodeFormule() {
        return codeFormule;
    }

    public void setCodeFormule(String codeFormule) {
        this.codeFormule = codeFormule;
    }

    @Override
    public String toString() {
        return "NotificationInformationDTO{" +
            "firebaseId='" + firebaseId + '\'' +
            ", msisdn='" + msisdn + '\'' +
            ", codeFormule='" + codeFormule + '\'' +
            '}';
    }
}
