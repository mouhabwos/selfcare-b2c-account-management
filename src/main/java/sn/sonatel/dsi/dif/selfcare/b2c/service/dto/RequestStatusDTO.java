package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import sn.sonatel.dsi.dif.selfcare.b2c.service.client.model.TroubleTicket;

public class RequestStatusDTO {

    private String requestId;
    private String status;
    private String title;
    private String description;
    private TroubleTicket.TicketTypeEnum type;
    private int order;
    private Boolean historic;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TroubleTicket.TicketTypeEnum getType() {
        return type;
    }

    public void setType(TroubleTicket.TicketTypeEnum type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Boolean getHistoric() {
        return historic;
    }

    public void setHistoric(Boolean historic) {
        this.historic = historic;
    }

}
