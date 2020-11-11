package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RattachementBaseClass;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RattachementLigneCNIVM  extends RattachementBaseClass {

    @ApiModelProperty(required = true)
    @NotNull
    @NotBlank
    private String identificationId;

    public String getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(String identificationId) {
        this.identificationId = identificationId;
    }
}
