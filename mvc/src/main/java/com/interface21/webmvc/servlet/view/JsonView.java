package com.interface21.webmvc.servlet.view;

import java.io.PrintWriter;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(model));
        writer.flush();
        response.setContentType("application/json");
    }
}
