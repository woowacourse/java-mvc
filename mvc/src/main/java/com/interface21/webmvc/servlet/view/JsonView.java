package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        try (PrintWriter writer = response.getWriter()) {
            if (model.size() == 1) {
                Object value = model.values().iterator().next();
                objectMapper.writeValue(writer, value);
                return;
            }
            objectMapper.writeValue(writer, model);
        }
    }
}
