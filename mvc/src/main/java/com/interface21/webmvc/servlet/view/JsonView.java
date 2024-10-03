package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
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
        ObjectMapper objectMapper = new ObjectMapper();
        String json = convertJson(model, objectMapper);
        response.getWriter().print(json);
    }

    private String convertJson(Map<String, ?> model, ObjectMapper objectMapper) throws JsonProcessingException {
        if (model.size() == 1) {
            return objectMapper.writeValueAsString(model.values().iterator().next());
        }

        return objectMapper.writeValueAsString(model);
    }
}
