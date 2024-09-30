package com.interface21.webmvc.servlet.view;

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
        if (model.size() == 1) {
            Object next = model.values().iterator().next();
            String json = mapper.writeValueAsString(next);
            response.getWriter().write(json);
            return;
        }
        String json = mapper.writeValueAsString(model);
        response.getWriter().write(json);
    }
}
