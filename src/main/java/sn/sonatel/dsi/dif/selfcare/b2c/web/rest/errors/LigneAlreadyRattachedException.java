package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

/**
 *
 * @since 23/05/2019
 * @author Bouya Kande
 *
 */
public class LigneAlreadyRattachedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LigneAlreadyRattachedException() {
        super ( "Ce numéro est rattaché à un compte", "userManagement", "userRattached" );
    }
}
