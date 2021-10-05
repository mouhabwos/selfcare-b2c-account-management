package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.MessageValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CheckNumberRequest {

    @ApiModelProperty(required = true)
    @NotBlank(message = "Le msisdn ne peut pas être vide")
    @NotNull(message = "Le msisdn ne doit pas être vide")
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = MessageValidation.NUMERO_ORANGE_VALIDE)
    private String msisdn;

    @ApiModelProperty(required = true)
    @NotBlank(message = "Le hmac ne peut pas être vide")
    @NotNull(message = "Le hmac ne doit pas être vide")
    private String hmac;

    private String uuid;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "CheckNumberRequest{" +
            "msisdn:'" + msisdn + '\'' +
            ", hmac:'" + hmac + '\'' +
            ", uuid:'" + uuid + '\'' +
            '}';
    }
}
