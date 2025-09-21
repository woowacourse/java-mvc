package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    ObjectMapper objectMapper = new ObjectMapper();

    public JsonView() {
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding("UTF-8");

        String body;
        if (model.size() == 1) {
            Object value = model.values().iterator().next();
            body = objectMapper.writeValueAsString(value);
        } else {
            body = objectMapper.writeValueAsString(model);
        }
        PrintWriter writer = response.getWriter();
        writer.write(body);
        writer.flush();

    }
}
