package sn.sonatel.dsi.dif.selfcare.b2c.service.client.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;

/**
  * Indicates the contact medium that could be used to contact the party.
 **/
@ApiModel(description="Indicates the contact medium that could be used to contact the party.")
public class ContactMedium {

  @ApiModelProperty(value = "Any additional characteristic(s) of this contact medium")
  @Valid
 /**
   * Any additional characteristic(s) of this contact medium
  **/
  private MediumCharacteristic characteristic = null;

  @ApiModelProperty(value = "Type of the contact medium, such as: email address, telephone number, postal address")
 /**
   * Type of the contact medium, such as: email address, telephone number, postal address
  **/
  private String mediumType = null;

  @ApiModelProperty(value = "If true, indicates that is the preferred contact medium")
 /**
   * If true, indicates that is the preferred contact medium
  **/
  private Boolean preferred = null;

}

