package sn.sonatel.dsi.dif.selfcare.b2c.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class EmailExistDTO {

    @Email(message = MessageValidation.EMAIL_VALID)
    @Size(min = 5, max = 254)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
