package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Object value = model;
        if (model.size() == 1) {
            value = model.get(model.keySet().iterator().next());
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), value);
    }
}
