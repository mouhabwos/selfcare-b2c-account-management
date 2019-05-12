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
    @NotNull(message = MessageValidation.FIELD_NUMERO)
    @NotBlank(message = MessageValidation.FIELD_NUMERO)
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = MessageValidation.FIELD_NUMERO_ORANGE)
    @Size(min = 9, max = 18, message = MessageValidation.FIELD_NUMERO_SIZE)
    private String msisdn;

    @NotNull(message = MessageValidation.FIELD_CODEOTP)
    @NotBlank(message = MessageValidation.FIELD_CODEOTP)
    @Size(min = 6, max = 6, message = MessageValidation.FIELD_CODEOTP_SIZE)
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
