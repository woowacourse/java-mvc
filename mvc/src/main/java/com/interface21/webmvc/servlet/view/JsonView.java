package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonView implements View {


    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapToJson(model, mapper);
        response.getWriter().write(json);
    }

    private static String mapToJson(Map<String, ?> model, ObjectMapper mapper) throws JsonProcessingException {
        if (model.size() == 1) {
            Object next = model.values().iterator().next();
            return mapper.writeValueAsString(next);
        }
        return mapper.writeValueAsString(model);
    }
}
