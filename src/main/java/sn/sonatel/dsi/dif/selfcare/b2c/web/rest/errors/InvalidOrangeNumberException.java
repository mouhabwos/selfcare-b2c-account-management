package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;


/**
 *
 * @author Bouya Kande
 * @since 1.1.4
 *
 */
public class InvalidOrangeNumberException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InvalidOrangeNumberException() {
        super ("Ce numéro doit être un numéro orange valide", "userManagement", "numberNoValid" );
    }
}
