package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValueAsString(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }
}
