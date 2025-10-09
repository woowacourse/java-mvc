package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final var writer = response.getWriter();

        if (model.isEmpty()) {
            writer.write("{}");
            return;
        }

        if (model.size() == 1) {
            final Object value = model.values().iterator().next();
            writer.write(objectMapper.writeValueAsString(value));
            return;
        }

        writer.write(objectMapper.writeValueAsString(model));
    }
}
