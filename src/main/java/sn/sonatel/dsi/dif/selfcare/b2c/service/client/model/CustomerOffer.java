package sn.sonatel.dsi.dif.selfcare.b2c.service.client.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferGammeEnum;
import sn.sonatel.dsi.dif.selfcare.b2c.domain.enumeration.OfferTypeEnum;

import javax.validation.Valid;

/**
 * @author BOUYA KANDE
 * @since 1.1.4
 *
 */

/**
 * The billing account receives all charges (recurring, one time and usage) of the offers and products assigned to it during order process. Periodically according to billing cycle specifications attached to the billing account or as a result of an event, a customer bill (aka invoice) is produced. This customer bill concerns different related parties which play a role on it : for example, a customer bill is produced by an operator, is sent to a bill receiver and has to be paid by a payer. A payment method could be assigned to the customer bill to build the call of payment. Lettering process enables to assign automatically or manually incoming amount from payments to customer bills (payment items). A tax item is created for each tax rate used in the customer bill. The financial account represents a financial entity which records all customer’s accounting events : payment amount are recorded as credit and invoices amount are recorded as debit. It gives the customer overall balance (account balance). The customer bill is linked to one or more documents that can be downloaded via a provided url.
 */
@ApiModel(description = "The billing account receives all charges (recurring, one time and usage) of the offers and products assigned to it during order process. Periodically according to billing cycle specifications attached to the billing account or as a result of an event, a customer bill (aka invoice) is produced. This customer bill concerns different related parties which play a role on it : for example, a customer bill is produced by an operator, is sent to a bill receiver and has to be paid by a payer. A payment method could be assigned to the customer bill to build the call of payment. Lettering process enables to assign automatically or manually incoming amount from payments to customer bills (payment items). A tax item is created for each tax rate used in the customer bill. The financial account represents a financial entity which records all customer’s accounting events : payment amount are recorded as credit and invoices amount are recorded as debit. It gives the customer overall balance (account balance). The customer bill is linked to one or more documents that can be downloaded via a provided url.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-08-01T13:22:16.481+02:00")

public class CustomerOffer   {
  @JsonProperty("clientCode")
  private String clientCode = null;




  @JsonProperty("createDate")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String createDate = null;

  @JsonProperty("data")
  private OfferBucket data = null;

  @JsonProperty("endUserId")
  private String endUserId = null;

  @JsonProperty("offerCode")
  private String offerCode = null;

  /**
   * Gets or Sets offerGamme
   */

  @JsonProperty("offerGamme")
  private OfferGammeEnum offerGamme = null;

  @JsonProperty("offerName")
  private String offerName = null;

  @JsonProperty("offerStatus")
  private String offerStatus = null;

  @JsonProperty("offerType")
  private OfferTypeEnum offerType = null;

  @JsonProperty("sms")
  private OfferBucket sms = null;

  @JsonProperty("voice")
  private OfferBucket voice = null;

  public CustomerOffer clientCode(String clientCode) {
    this.clientCode = clientCode;
    return this;
  }

   /**
   * Get clientCode
   * @return clientCode
  **/
  @ApiModelProperty(value = "")


  public String getClientCode() {
    return clientCode;
  }

  public void setClientCode(String clientCode) {
    this.clientCode = clientCode;
  }

  public CustomerOffer createDate(String createDate) {
    this.createDate = createDate;
    return this;
  }

   /**
   * Get createDate
   * @return createDate
  **/
  @ApiModelProperty(value = "")

  @Valid

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public CustomerOffer data(OfferBucket data) {
    this.data = data;
    return this;
  }

   /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OfferBucket getData() {
    return data;
  }

  public void setData(OfferBucket data) {
    this.data = data;
  }

  public CustomerOffer endUserId(String endUserId) {
    this.endUserId = endUserId;
    return this;
  }

   /**
   * Get endUserId
   * @return endUserId
  **/
  @ApiModelProperty(value = "")


  public String getEndUserId() {
    return endUserId;
  }

  public void setEndUserId(String endUserId) {
    this.endUserId = endUserId;
  }

  public CustomerOffer offerCode(String offerCode) {
    this.offerCode = offerCode;
    return this;
  }

   /**
   * Get offerCode
   * @return offerCode
  **/
  @ApiModelProperty(value = "")


  public String getOfferCode() {
    return offerCode;
  }

  public void setOfferCode(String offerCode) {
    this.offerCode = offerCode;
  }

  public CustomerOffer offerGamme(OfferGammeEnum offerGamme) {
    this.offerGamme = offerGamme;
    return this;
  }

   /**
   * Get offerGamme
   * @return offerGamme
  **/
  @ApiModelProperty(value = "")


  public OfferGammeEnum getOfferGamme() {
    return offerGamme;
  }

  public void setOfferGamme(OfferGammeEnum offerGamme) {
    this.offerGamme = offerGamme;
  }

  public CustomerOffer offerName(String offerName) {
    this.offerName = offerName;
    return this;
  }

   /**
   * Get offerName
   * @return offerName
  **/
  @ApiModelProperty(value = "")


  public String getOfferName() {
    return offerName;
  }

  public void setOfferName(String offerName) {
    this.offerName = offerName;
  }

  public CustomerOffer offerStatus(String offerStatus) {
    this.offerStatus = offerStatus;
    return this;
  }

   /**
   * Get offerStatus
   * @return offerStatus
  **/
  @ApiModelProperty(value = "")


  public String getOfferStatus() {
    return offerStatus;
  }

  public void setOfferStatus(String offerStatus) {
    this.offerStatus = offerStatus;
  }

  public CustomerOffer offerType(OfferTypeEnum offerType) {
    this.offerType = offerType;
    return this;
  }

   /**
   * Get offerType
   * @return offerType
  **/
  @ApiModelProperty(value = "")


  public OfferTypeEnum getOfferType() {
    return offerType;
  }

  public void setOfferType(OfferTypeEnum offerType) {
    this.offerType = offerType;
  }

  public CustomerOffer sms(OfferBucket sms) {
    this.sms = sms;
    return this;
  }

   /**
   * Get sms
   * @return sms
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OfferBucket getSms() {
    return sms;
  }

  public void setSms(OfferBucket sms) {
    this.sms = sms;
  }

  public CustomerOffer voice(OfferBucket voice) {
    this.voice = voice;
    return this;
  }

   /**
   * Get voice
   * @return voice
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OfferBucket getVoice() {
    return voice;
  }

  public void setVoice(OfferBucket voice) {
    this.voice = voice;
  }

    @Override
    public String toString() {
        return "{" +
            "\"clientCode\":" + clientCode + ',' +
            "\"createDate\":" + createDate + ',' +
            "\"data\":" + data +
            "\"endUserId\":" + endUserId + ',' +
            "\"offerCode\":" + offerCode + ',' +
            "\"offerGamme\":" + offerGamme +
            "\"offerName\":" + offerName + ',' +
            "\"offerStatus\":" + offerStatus + ',' +
            "\"offerType\"" + offerType + ',' +
            "\"sms\":" + sms + ',' +
            "\"voice\":" + voice + ',' +
            '}';
    }
}

