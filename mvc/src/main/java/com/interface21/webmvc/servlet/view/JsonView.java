package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Object data = model;

        if (model.size() == 1) {
            data = model.values().iterator().next();
        }

        String json = objectMapper.writeValueAsString(data);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(json);
    }
}
