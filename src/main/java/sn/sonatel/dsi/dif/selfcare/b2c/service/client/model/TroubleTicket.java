package sn.sonatel.dsi.dif.selfcare.b2c.service.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * A trouble ticket is a record of an issue that is created, tracked, and managed by a trouble ticket management system
 **/
@ApiModel(
    description = "A trouble ticket is a record of an issue that is created, tracked, and managed by a trouble ticket management system"
)
public class TroubleTicket {

    @ApiModelProperty(value = "")
    @Valid
    private List<ContactMedium> contactMedium = null;

    @ApiModelProperty(value = "The date on which the trouble ticket was created")
    /**
     * The date on which the trouble ticket was created
     **/
    private LocalDateTime creationDate = null;

    @ApiModelProperty(value = "Description of the trouble or issue")
    /**
     * Description of the trouble or issue
     **/
    private String description = null;

    @ApiModelProperty(value = "Unique identifier of the trouble ticket")
    /**
     * Unique identifier of the trouble ticket
     **/
    private String id = null;

    @ApiModelProperty(value = "The date and time that the trouble ticked was last updated")
    /**
     * The date and time that the trouble ticked was last updated
     **/
    private LocalDateTime lastUpdate = null;

    @ApiModelProperty(value = "The note(s) that are associated to the ticket.")
    @Valid
    /**
     * The note(s) that are associated to the ticket.
     **/
    private List<Note> note = null;

    @ApiModelProperty(value = "")
    @Valid
    private List<RelatedParty> relatedParty = null;

    @ApiModelProperty(value = "The date and time the trouble ticket was resolved")
    /**
     * The date and time the trouble ticket was resolved
     **/
    private LocalDateTime resolutionDate = null;

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @XmlType(name = "StatusEnum")
    @XmlEnum(String.class)
    public enum StatusEnum {
        @XmlEnumValue("ACKNOWLEDGED")
        ACKNOWLEDGED(String.valueOf("ACKNOWLEDGED")),
        @XmlEnumValue("REJECTED")
        REJECTED(String.valueOf("REJECTED")),
        @XmlEnumValue("PENDING")
        PENDING(String.valueOf("PENDING")),
        @XmlEnumValue("HELD")
        HELD(String.valueOf("HELD")),
        @XmlEnumValue("INPROGRESS")
        INPROGRESS(String.valueOf("INPROGRESS")),
        @XmlEnumValue("CANCELLED")
        CANCELLED(String.valueOf("CANCELLED")),
        @XmlEnumValue("CLOSED")
        CLOSED(String.valueOf("CLOSED")),
        @XmlEnumValue("RESOLVED")
        RESOLVED(String.valueOf("RESOLVED")),
        @XmlEnumValue("UNKNOWN")
        UNKNOWN(String.valueOf("UNKNOWN"));

        private String value;

        StatusEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }
    }

    @ApiModelProperty(value = "The current status of the trouble ticket")
    /**
     * The current status of the trouble ticket
     **/
    private StatusEnum status = null;

    @ApiModelProperty(value = "The date and time the status changed.")
    /**
     * The date and time the status changed.
     **/
    private LocalDateTime statusChangeDate = null;

    @XmlType(name = "TicketTypeEnum")
    @XmlEnum(String.class)
    public enum TicketTypeEnum {
        @XmlEnumValue("INCIDENT")
        INCIDENT(String.valueOf("INCIDENT")),
        @XmlEnumValue("REQUEST")
        REQUEST(String.valueOf("REQUEST"));

        private String value;

        TicketTypeEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    @ApiModelProperty(value = "represent a business type of the trouble ticket e.g. incident, complain, request")
    /**
     * represent a business type of the trouble ticket e.g. incident, complain, request
     **/
    private TicketTypeEnum ticketType = null;

    /**
     * Unique identifier of the trouble ticket
     * @return id
     **/
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TroubleTicket id(String id) {
        this.id = id;
        return this;
    }

    /**
     * The current status of the trouble ticket
     * @return status
     **/
    @JsonProperty("status")
    public String getStatus() {
        if (status == null) {
            return null;
        }
        return status.value();
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
     * represent a business type of the trouble ticket e.g. incident, complain, request
     * @return ticketType
     **/
    @JsonProperty("ticketType")
    public String getTicketType() {
        if (ticketType == null) {
            return null;
        }
        return ticketType.value();
    }

    public void setTicketType(TicketTypeEnum ticketType) {
        this.ticketType = ticketType;
    }
}
