package sn.sonatel.dsi.dif.selfcare.b2c.service.client.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
  * Describes the contact medium characteristics that could be used to contact for the trouble ticket
 **/
@ApiModel(description="Describes the contact medium characteristics that could be used to contact for the trouble ticket")
public class MediumCharacteristic {

  @ApiModelProperty(value = "The type of contact, for example: phone number such as mobile, fixed home, fixed office. postal address such as shipping instalation…")
 /**
   * The type of contact, for example: phone number such as mobile, fixed home, fixed office. postal address such as shipping instalation…
  **/
  private String contactType = null;

  @ApiModelProperty(value = "The primary phone number of the contact")
 /**
   * The primary phone number of the contact
  **/
  private String phoneNumber = null;
}

