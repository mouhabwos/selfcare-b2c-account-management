package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;


/**
 *
 * @author Bouya Kande
 * @since 1.1.4
 *
 */
public class NoValideNumberFixeException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NoValideNumberFixeException() {
        super ("Ce numéro doit être un numéro fixe orange valide", "userManagement", "numberFixeNoValid" );
    }
}
