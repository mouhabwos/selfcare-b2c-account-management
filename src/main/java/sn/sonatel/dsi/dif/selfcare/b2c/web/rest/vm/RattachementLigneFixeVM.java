package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.MessageValidation;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.RattachementBaseClass;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author BOUYA KANDE
 * @since 1.1.4
 */
public class RattachementLigneFixeVM extends RattachementBaseClass {

    @ApiModelProperty(required = true)
    @NotNull(message = MessageValidation.ID_CLIENT)
    @NotBlank(message = MessageValidation.ID_CLIENT)
    private String idClient;

    public RattachementLigneFixeVM() {
        //Default Constructor
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }
}
