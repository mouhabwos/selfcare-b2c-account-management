package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link sn.sonatel.dsi.dif.selfcare.b2c.domain.Sponsee} entity.
 */
public class SponseeDTO {

    private Long id;

    @NotNull
    private String msisdn;

    private String firstName;

    private String lastName;

    @NotNull
    private Boolean effective = false;

    @NotNull
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @NotNull
    private Boolean enabled = true;

    @NotNull
    @NotBlank
    private String msisdnSponsor;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean isEffective() {
        return effective;
    }

    public void setEffective(Boolean effective) {
        this.effective = effective;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getMsisdnSponsor() {
        return msisdnSponsor;
    }

    public void setMsisdnSponsor(String msisdnSponsor) {
        this.msisdnSponsor = msisdnSponsor;
    }

    @Override
    public String toString() {

        return "{" +
            "\"id\":" + id + ',' +
            "\"msisdn\":" + msisdn + ',' +
            "\"firstName\":" + firstName + ',' +
            "\"lastName\":" + lastName + ',' +
            "\"effective\":" + isEffective() + ',' +
            "\"createdDate\":" + getCreatedDate() + ',' +
            "\"enabled\":" + isEnabled() +
            "\"msisdnSponsor\":" + msisdnSponsor +
            '}';
    }
}
