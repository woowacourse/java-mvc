package com.interface21.webmvc.servlet.view;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final String data = makeData(model, response);
        response.getWriter().write(data);
    }

    private String makeData(final Map<String, ?> model, final HttpServletResponse response)
            throws JsonProcessingException {
        if (model.isEmpty()) {
            return "";
        }
        if (model.size() == 1) {
            final String key = model.keySet().iterator().next();
            return model.get(key).toString();
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(model);
    }
}
