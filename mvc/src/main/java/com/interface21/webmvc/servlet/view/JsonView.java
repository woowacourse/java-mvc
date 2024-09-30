package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private static final int SINGLE_DATA_SIZE = 1;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        writeAsJson(model, response);
    }

    private void writeAsJson(Map<String, ?> model, HttpServletResponse response) throws IOException {
        if (model.size() == SINGLE_DATA_SIZE) {
            Object object = model.values().stream()
                    .findFirst()
                    .get();
            response.getWriter().write(objectMapper.writeValueAsString(object));
            return;
        }

        response.getWriter().write(objectMapper.writeValueAsString(model));
    }
}
