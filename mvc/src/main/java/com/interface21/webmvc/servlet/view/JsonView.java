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
        final var mapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final var outputStream = response.getOutputStream();

        if (model.size() == 1) {
            mapper.writeValue(outputStream, model.values().iterator().next());
            return;
        }

        mapper.writeValue(outputStream, model);
    }
}
