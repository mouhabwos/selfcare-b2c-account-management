package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Simple exception with a message, that returns an Internal Server Error code.
 */
public class ServiceUnavailableException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ServiceUnavailableException(String message) {
        super ( ErrorConstants.DEFAULT_TYPE, message, Status.SERVICE_UNAVAILABLE );
    }
}
