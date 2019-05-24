package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

/**
 *
 * @author Bouya Kande
 * @since 1.1.4
 *
 */
public class AccountAlreadyHaveNumberFixeException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public AccountAlreadyHaveNumberFixeException() {
        super ("Un numéro fixe est déjà rattaché à ce compte", "userManagement", "numberFixe" );
    }
}
