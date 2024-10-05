package com.interface21.webmvc.servlet.view;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    private final ObjectMapper mapper;

    public JsonView() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final String value = convertToJson(model);
        response.getWriter().write(value);
    }

    private String convertToJson(final Map<String, ?> model) throws JsonProcessingException {
        if (hasOnlyOneKey(model)) {
            final Object value = model.values().iterator().next();
            return mapper.writeValueAsString(value);
        }
        return mapper.writeValueAsString(model);
    }

    private boolean hasOnlyOneKey(final Map<String, ?> model) {
        return model.keySet().size() == 1;
    }
}
