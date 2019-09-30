package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

public class InfoNumberVM {

    private String msisdn;

    private String profil;

    private String formule;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getFormule() {
        return formule;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    @Override
    public String toString() {
        return "{" +
            "\"msisdn\":" + msisdn + ',' +
            "\"profil\":" + profil + ',' +
            "\"formule\":" + formule + ',' +
            '}';
    }
}
