package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final String DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;
    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String json = getJsonString(model);

        response.setContentType(DEFAULT_CONTENT_TYPE);
        response.getWriter().write(json);
    }

    private String getJsonString(Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            throw new IllegalArgumentException("Empty model");
        }
        if (model.size() == 1) {
            Object value = model.values().iterator().next();
            return objectMapper.writeValueAsString(value);
        }
        return objectMapper.writeValueAsString(model);
    }
}
