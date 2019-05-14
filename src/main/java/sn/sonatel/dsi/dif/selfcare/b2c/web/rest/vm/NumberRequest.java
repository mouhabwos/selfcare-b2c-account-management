package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NumberRequest {

    private String msisdn;

    @ApiModelProperty(required = true)
    @NotBlank(message = "Le token ne peut pas être vide")
    @NotNull(message = "Le token ne doit pas être vide")
    private String token;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
