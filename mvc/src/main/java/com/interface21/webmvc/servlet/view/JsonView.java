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
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final Object responseData;
        if (model.size() == 1) {
            responseData = model.values().iterator().next();
        } else {
            responseData = model;
        }

        final String jsonResponse = objectMapper.writeValueAsString(responseData);
        response.getWriter().write(jsonResponse);
    }
}
