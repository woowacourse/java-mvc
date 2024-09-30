package com.interface21.webmvc.servlet.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        final ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model.keySet().size() == 1) {
            final PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(model.values().iterator().next()));
        }
    }
}
