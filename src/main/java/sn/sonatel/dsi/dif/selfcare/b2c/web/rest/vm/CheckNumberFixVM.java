package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.vm;

import sn.sonatel.dsi.dif.selfcare.b2c.config.Constants;
import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.MessageValidation;

import javax.validation.constraints.Pattern;

/**
 *
 * @since 23/05/2019
 * @author Bouya Kande
 *
 */
public class CheckNumberFixVM extends NumberRequest{

    @Pattern(regexp = Constants.LOGIN_REGEX_VALID_NUMBER, message = MessageValidation.NUMERO_ORANGE_VALIDE)
    private String login;

    public CheckNumberFixVM() {

        //Default Constructor
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
