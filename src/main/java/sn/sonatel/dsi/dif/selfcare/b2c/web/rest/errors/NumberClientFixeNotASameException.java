package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;


/**
 *
 * @author Bouya Kande
 * @since 1.1.4
 *
 */
public class NumberClientFixeNotASameException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NumberClientFixeNotASameException() {
        super ("L'id client saisie n'est pas valide", "userManagement", "numberFixeNoValid" );
    }
}
