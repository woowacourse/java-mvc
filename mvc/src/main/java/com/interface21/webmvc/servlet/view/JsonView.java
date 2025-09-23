package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String body = createResponseBody(model);
        writeJsonResponse(response, body);
    }

    private String createResponseBody(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            final Object value = model.values().iterator().next();
            return objectMapper.writeValueAsString(value);
        }
        return objectMapper.writeValueAsString(model);
    }

    private void writeJsonResponse(HttpServletResponse response, String body) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);
    }
}
