package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

public class LigneNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LigneNotFoundException() {
        super("Ce numéro n'existe pas", "userManagement", "userRattached");
    }
}
