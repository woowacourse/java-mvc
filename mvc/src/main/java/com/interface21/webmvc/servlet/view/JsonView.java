package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Object renderObject = getRenderObject(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String output = serialize(renderObject);
        view(output, response);
    }

    private Object getRenderObject(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }

    private String serialize(Object renderObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(renderObject);
    }

    private void view(String output, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.print(output);
    }
}
