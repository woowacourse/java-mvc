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

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int RETURN_ORIGIN_VALUE = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = toJson(model);
        response.getWriter().write(json);
    }

    private String toJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == RETURN_ORIGIN_VALUE) {
            Object object = model.values().iterator().next();
            return objectMapper.writeValueAsString(object);
        }
        return objectMapper.writeValueAsString(model);
    }
}
