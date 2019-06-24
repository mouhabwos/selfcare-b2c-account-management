package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 *
 * @author Bouya Kande
 * @since 1.1.4
 *
 */
public class NumeroClientNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public NumeroClientNotFoundException() {
        super ( null,"Id client non trouvé ", Status.NOT_FOUND );
    }
}
