package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

import sn.sonatel.dsi.dif.selfcare.b2c.service.dto.MessageValidation;

public class NoValideNumberFixeException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public NoValideNumberFixeException() {
        super (MessageValidation.NUMERO_ORANGE_VALIDE, "userManagement", "numberFixeNoValid" );
    }
}
