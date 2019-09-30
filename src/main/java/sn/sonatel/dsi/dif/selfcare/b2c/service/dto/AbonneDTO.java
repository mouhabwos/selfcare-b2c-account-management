package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by centonni on 15/11/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbonneDTO {


    private String nomAbonne;

    private String prenomAbonne;


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

    @Override
    public String toString() {
        return "{" +
            "\"nomAbonne\":" + nomAbonne + ',' +
            "\"prenomAbonne\":" + prenomAbonne +
            '}';
    }
}
