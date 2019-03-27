package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SOAPRequest {

    @NotNull
    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER)
    public String msisdn;

    public SOAPRequest(String msisdn) {
        this.msisdn = msisdn;
        // Default Constructor
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

}
