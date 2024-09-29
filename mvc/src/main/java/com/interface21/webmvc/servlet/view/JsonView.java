package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String rawJson = serializeModel(model);
        response.getWriter().write(rawJson);
    }

    private String serializeModel(Map<String, ?> model) throws JsonProcessingException {
        Object target = model;
        if (hasSingleEntry(model)) {
            target = model.values().stream()
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
        }
        return objectMapper.writeValueAsString(target);
    }

    private boolean hasSingleEntry(Map<String, ?> model) {
        return model.size() == 1;
    }
}
