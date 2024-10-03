package com.interface21.webmvc.servlet.view;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = serialize(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);
    }

    private String serialize(Map<String, ?> model) throws JsonProcessingException {
        Object body = model;
        if (isSingleModel(model)) {
            body = model.values().stream()
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
        }
        return objectMapper.writeValueAsString(body);
    }

    private boolean isSingleModel(Map<String, ?> model) {
        return model.size() == 1;
    }
}
