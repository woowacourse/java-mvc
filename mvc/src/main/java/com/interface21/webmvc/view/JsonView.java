package com.interface21.webmvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        Object body = getBody(model);

        String jsonBody = objectMapper.writeValueAsString(body);
        response.getWriter().write(jsonBody);
    }

    private Object getBody(final Map<String, ?> model) {
        if (model == null || model.isEmpty()) {
            return Collections.emptyMap();
        }
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }
}
