package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ForbiddenException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ForbiddenException() {
        super (null, "Vous n'êtes pas autorisé à accéder à cette ressource", Status.FORBIDDEN );
    }
}
