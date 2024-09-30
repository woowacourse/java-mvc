package com.interface21.webmvc.servlet.view;

import java.io.PrintWriter;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String value = getModelAsString(model);
        PrintWriter writer = response.getWriter();
        writer.write(value);
        writer.flush();
        response.setContentType("application/json");
    }

    private String getModelAsString(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return writeSingleObjectValue(model);
        }
        return objectMapper.writeValueAsString(model);
    }

    private String writeSingleObjectValue(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() != 1) {
            throw new IllegalStateException();
        }
        String key = model.keySet().stream()
                .findFirst()
                .get();
        return objectMapper.writeValueAsString(model.get(key));
    }
}
