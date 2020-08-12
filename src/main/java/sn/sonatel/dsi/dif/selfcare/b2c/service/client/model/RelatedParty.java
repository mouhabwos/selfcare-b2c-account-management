package sn.sonatel.dsi.dif.selfcare.b2c.service.client.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
  * Related Entity reference. A related party defines party or party role linked to a specific entity.
 **/
@ApiModel(description="Related Entity reference. A related party defines party or party role linked to a specific entity.")
public class RelatedParty  {

  @ApiModelProperty(required = true, value = "Unique identifier of a related entity.")
 /**
   * Unique identifier of a related entity.
  **/
  private String id = null;

  @ApiModelProperty(value = "Name of the related entity.")
 /**
   * Name of the related entity.
  **/
  private String name = null;

  @ApiModelProperty(value = "Role played by the related party")
 /**
   * Role played by the related party
  **/
  private String role = null;
}

