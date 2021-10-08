package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ResetPasswordVM {

    @NotNull
    @NotEmpty
    private String login;

    @NotNull
    @NotEmpty
    private String hmac;

    private String newPassword;
}
