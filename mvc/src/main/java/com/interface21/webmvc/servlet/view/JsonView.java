package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        Object responseBody = getResponseBody(model);

        String jsonResponseBody = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(jsonResponseBody);
    }

    private Object getResponseBody(Map<String, ?> model) {
        return model.size() == 1 ? model.values().iterator().next() : model;
    }
}
