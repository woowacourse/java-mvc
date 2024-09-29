package com.interface21.webmvc.servlet.view;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {
    private static final int SINGLE_MODEL_SIZE = 1;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding("UTF-8");

        if (model.size() == SINGLE_MODEL_SIZE) {
            final Object singleValue = model.values().iterator().next();
            objectMapper.writeValue(response.getWriter(), singleValue);
            return;
        }

        objectMapper.writeValue(response.getWriter(), model);
    }
}
