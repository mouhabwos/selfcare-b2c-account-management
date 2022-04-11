package sn.sonatel.dsi.dif.selfcare.b2c.service.client.keycloak.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserCredentialDTO {

    @ApiParam(value = "username", example = "maurice@gmail.com")
    @NotBlank(message = "username is Empty")
    @NotNull(message = "username is null")
    private String username;

    @ApiParam(value = "password", example = "azerty")
    @NotBlank(message = "password is empty")
    @NotNull(message = "password is null")
    private String password;
}
