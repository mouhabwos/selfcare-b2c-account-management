package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by centonni on 15/11/18.
 */
public class CodeOTPCheckDTO {

    @ApiModelProperty(required = true)
    @NotNull(message = "Le numero ne peut pas être vide")
    @NotBlank(message = "Le numéro ne doit pas être vide")
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = "Le numéro doit un numéro orange valide")
    @Size(min = 9, max = 18, message = "La taille du numéro doit être de 9 chiffres")
    private String msisdn;

    @NotNull(message = "Le code ne peut pas être vide")
    @NotBlank(message = "Le code ne doit pas être vide")
    @Size(min = 6, max = 6, message = "La taille du code doit être de 6 chiffres")
    private String code;

    private boolean valid = false;





    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "CodeOTPCheckVM{" + "msisdn='" + msisdn + '\'' + ", code='" + "******" + '\''
            + ", valid=" + valid + '}';
    }

}
