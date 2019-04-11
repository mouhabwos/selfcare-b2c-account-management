package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.HashMap;
import java.util.Map;

public class FileManagerException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;
    private String entityName;
    private String errorKey;

    public FileManagerException( String defaultMessage, String entityName, String errorKey) {
        super ( null, defaultMessage, Status.INTERNAL_SERVER_ERROR, null, null, null, getAlertParameters ( entityName, errorKey ) );
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put ( "message", "error." + errorKey );
        parameters.put ( "params", entityName );
        return parameters;
    }
}
