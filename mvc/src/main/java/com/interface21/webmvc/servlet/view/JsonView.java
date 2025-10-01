package com.interface21.webmvc.servlet.view;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = OBJECT_MAPPER.writeValueAsString(model);
        response.getWriter().write(json);
    }
}
