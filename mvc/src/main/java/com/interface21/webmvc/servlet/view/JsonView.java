package com.interface21.webmvc.servlet.view;

import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String body = getBody(model);
        response.getWriter().write(body);
    }

    private String getBody(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            Object object = model.values().iterator().next();
            return objectMapper.writeValueAsString(object);
        }
        return objectMapper.writeValueAsString(model);
    }
}
