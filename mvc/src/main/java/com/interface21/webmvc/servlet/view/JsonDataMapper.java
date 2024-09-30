package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class JsonDataMapper {

    private static final int SINGLE = 1;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getData(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == SINGLE) {
            Object value = model.values().iterator().next();
            return value.toString();
        }
        return objectMapper.writeValueAsString(model);
    }
}
