package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {
    private static final String ENCODING = "UTF-8";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(ENCODING);

        Object value = getValue(model);
        String json = objectMapper.writeValueAsString(value);
        response.getWriter().write(json);
    }

    private Object getValue(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }
}
