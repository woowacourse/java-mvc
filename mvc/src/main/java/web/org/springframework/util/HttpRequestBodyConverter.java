package web.org.springframework.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpRequestBodyConverter {

    public static Object serialize(final String json, final Class<?> target) throws Exception {
        return new ObjectMapper().readValue(json, target);
    }

    public static String deserialize(final Object object) throws Exception {
        return new ObjectMapper().writeValueAsString(object);
    }
}
