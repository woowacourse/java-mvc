package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        try (final var writer = response.getWriter()) {
            final var objectMapper = new ObjectMapper();
            if (model.size() == 1) {
                final var value = model.values().iterator().next();
                final var json = objectMapper.writeValueAsString(value);
                writer.print(json);
                return;
            }
            final var json = objectMapper.writeValueAsString(model);
            writer.print(json);
        }
    }
}
