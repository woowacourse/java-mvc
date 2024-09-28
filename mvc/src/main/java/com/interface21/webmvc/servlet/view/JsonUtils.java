package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String EMPTY_STRING = "";

    static {
        OBJECT_MAPPER.setVisibility(
                OBJECT_MAPPER.getSerializationConfig()
                        .getDefaultVisibilityChecker()
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }

    private JsonUtils() {
    }

    public static String toJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            return EMPTY_STRING;
        }
        if (model.size() == 1) {
            Object object = model.values().iterator().next();
            return OBJECT_MAPPER.writeValueAsString(object);
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
