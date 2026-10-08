package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this(new ObjectMapper());
    }

    JsonView(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        objectMapper.writeValue(response.getWriter(), content(model));
    }

    private Object content(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }
}
