package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.errors;


import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.util.HashMap;
import java.util.Map;

public class NotFoundNumberException extends AbstractThrowableProblem {

    public NotFoundNumberException(String param) {

        super ( null, "Offer not found", Status.NOT_FOUND, "code:60", null, null,  getAlertParameters(param, "") );

    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put ( "message", "Error Offer not found." + errorKey );
        parameters.put ( "params", entityName );
        return parameters;
    }


}
