package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Ce numéro est utilisé", "userManagement", "userexists");
    }
}
