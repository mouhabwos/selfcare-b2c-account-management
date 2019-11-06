package sn.sonatel.dsi.dif.selfcare.b2c.service.vm;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.MessageValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MessageVM {

    @ApiModelProperty(required = true)
    @NotNull(message = MessageValidation.NUMERO_NON_VIDE)
    @NotBlank(message = MessageValidation.NUMERO_NON_VIDE)
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = MessageValidation.NUMERO_ORANGE_VALIDE)
    @Size(min = 9, max = 18, message = MessageValidation.NUMERO_TAILLE_VALIDE)
    private String msisdn = "";

    @NotNull(message = MessageValidation.MESSAGE_NON_VIDE)
    @NotBlank(message = MessageValidation.MESSAGE_NON_VIDE)
    private String message = "";

    private  String sourceAddress;

    public MessageVM() {
        //Default constructor
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

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }
}
