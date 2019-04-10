package sn.sonatel.dsi.dif.selfcare.b2c.service.vm;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MessageVM {

    @ApiModelProperty(required = true)
    @NotNull(message = "Le numero ne peut pas être vide")
    @NotBlank(message = "Le numéro ne doit pas être vide")
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = "Le numéro doit un numéro orange valide")
    @Size(min = 9, max = 18, message = "La taille du numéro doit être de 9 chiffres")
    private String msisdn;

    @NotNull(message = "Le message ne doit pas être vide")
    @NotBlank(message = "Le message ne doit pas être vide")
    private String message;

    public MessageVM(String msisdn, String message) {
        this.msisdn = msisdn;
        this.message = message;
    }

    public MessageVM() {
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
