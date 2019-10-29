package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;



public class MsisdnAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public MsisdnAlreadyUsedException() {
        super ( "Ce numéro est deja  utilisé", "Sponsor", "sponsorAdded" );
    }
}
