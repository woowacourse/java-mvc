package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final Object payload = resolvePayload(model);
        objectMapper.writeValue(response.getWriter(), payload);
    }

    private Object resolvePayload(final Map<String, ?> model) {
        if (model == null || model.isEmpty()) {
            return Map.of();
        }

        if (model.size() == 1) {
            return model.values().iterator().next();
        }

        return model;
    }
}
