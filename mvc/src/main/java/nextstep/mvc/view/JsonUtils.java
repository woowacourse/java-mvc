package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtils() {
    }

    public static String writeValueAsString(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
}
