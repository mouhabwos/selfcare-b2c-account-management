package sn.sonatel.dsi.dif.selfcare.b2c.service.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */

/**
 * OfferBucket
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-08-01T13:22:16.481+02:00")

public class OfferBucket   {
  @JsonProperty("unit")
  private String unit = null;

  @JsonProperty("value")
  private String value = null;

  public OfferBucket unit(String unit) {
    this.unit = unit;
    return this;
  }

   /**
   * Get unit
   * @return unit
  **/
  @ApiModelProperty(value = "")


  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public OfferBucket value(String value) {
    this.value = value;
    return this;
  }

   /**
   * Get value
   * @return value
  **/
  @ApiModelProperty(value = "")


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }




}

