package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

/**
 *
 * @since 23/05/2019
 * @author Bouya Kande
 *
 */
public class AccountAlreadyHaveNumberFixeException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public AccountAlreadyHaveNumberFixeException() {
        super ("Un numéro fixe est déjà rattaché à ce compte", "userManagement", "numberFixe" );
    }
}
