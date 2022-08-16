package sn.sonatel.dsi.dif.selfcare.b2c.web.rest.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.SneakyThrows;

public final class LogUtil {

    private LogUtil() {
        //private constructor
    }

    public static synchronized ObjectMapper getObjectMapper() {
        return JsonMapper
            .builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    }

    @SneakyThrows
    public static String convertObjectToJsonResponse(Object o) {
        return getObjectMapper().writeValueAsString(o);
    }
}
