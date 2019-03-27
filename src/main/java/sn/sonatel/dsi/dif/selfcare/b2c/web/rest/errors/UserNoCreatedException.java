package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

public class UserNoCreatedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public UserNoCreatedException() {
        super ( "Utilisateur non créé", "userManagement", "nocreated" );
    }
}
