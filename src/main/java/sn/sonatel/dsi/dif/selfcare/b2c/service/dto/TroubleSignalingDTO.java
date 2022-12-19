package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TroubleSignalingDTO {

    @JsonProperty(required = true)
    private String msisdn;

    private String email;

    @JsonProperty(required = true)
    private Type type;

    private String motif;
    private String message;

    @JsonProperty(value = "num_fix", required = true)
    private String numFix;

    @JsonProperty(value = "id_request")
    private String idRequest;

    public enum Type {
        DERANGEMENT,
        RECLAMATION,
    }
}
