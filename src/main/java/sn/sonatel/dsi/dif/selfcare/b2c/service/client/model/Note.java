package sn.sonatel.dsi.dif.selfcare.b2c.service.client.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
  * Extra information about a given entity
 **/
@ApiModel(description="Extra information about a given entity")
public class Note  {

  @ApiModelProperty(value = "Author of the note")

  private String author = null;

  @ApiModelProperty(value = "Text of the note")
 /**
   * Text of the note
  **/
  private String text = null;

}

