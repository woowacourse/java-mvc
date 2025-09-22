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
        final var writer = response.getWriter();

        if (model.size() == 1) {
            final var only = model.entrySet().iterator().next().getValue();
            writer.print(only);
            return;
        }
        final var objectMapper = new ObjectMapper();
        final var json = objectMapper.writeValueAsString(model);
        writer.print(json);
    }
}
