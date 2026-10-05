package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        Object value = model;
        if (model.size() == 1) {
            value = model.values().iterator().next();
        }

        String jsonStr = OBJECT_MAPPER.writeValueAsString(value);

        response.getWriter().write(jsonStr);
    }
}
