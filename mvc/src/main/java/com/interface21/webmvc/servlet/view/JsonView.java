package com.interface21.webmvc.servlet.view;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (model.size() == 1) {
            final String key = model.keySet().iterator().next();
            final String data = model.get(key).toString();
            response.getWriter().write(data);
            return;
        }
        if (model.size() >= 2) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            final ObjectMapper objectMapper = new ObjectMapper();
            final String data = objectMapper.writeValueAsString(model);
            response.getWriter().write(data);
        }
    }
}
