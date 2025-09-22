package com.interface21.webmvc.servlet.view;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        Object value = extractValue(model);
        String jsonValue = objectMapper.writeValueAsString(value);
        log.debug("jsonValue: {}", jsonValue);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (final var writer = response.getWriter()) {
            writer.write(jsonValue);
            writer.flush();
        }
    }

    private Object extractValue(final Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }
}
