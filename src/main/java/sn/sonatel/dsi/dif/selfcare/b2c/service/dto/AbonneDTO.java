package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util.LogUtil;

/**
 * Created by centonni on 15/11/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbonneDTO {


    private String nomAbonne;

    private String prenomAbonne;

    private String msisdn;

    public String getNomAbonne() {
        return nomAbonne;
    }

    public void setNomAbonne(String nomAbonne) {
        this.nomAbonne = nomAbonne;
    }

    public String getPrenomAbonne() {
        return prenomAbonne;
    }

    public void setPrenomAbonne(String prenomAbonne) {
        this.prenomAbonne = prenomAbonne;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @Override
    public String toString() {
        return LogUtil.convertObjectToJsonResponse(this);
    }
}
