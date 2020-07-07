package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

import lombok.SneakyThrows;
import org.codehaus.jackson.map.ObjectMapper;


public final class LogUtil {

    private static ObjectMapper objectMapper = null;

    private LogUtil() {
        //private constructor
    }

    public static synchronized ObjectMapper getObjectMapper(){
        if (objectMapper == null)
        {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    @SneakyThrows
    public static String convertObjectToJsonResponse(Object o){
        return getObjectMapper().writeValueAsString(o);
    }
}
