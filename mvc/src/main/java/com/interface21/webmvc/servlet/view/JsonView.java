package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final String JSON_CONTENT_TYPE = "application/json;charset=UTF-8";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.setContentType(JSON_CONTENT_TYPE);

        Object data = parseModelToObject(model);
        String json = objectMapper.writeValueAsString(data);
        log.info("json : {}", json);
        try (final var writer = response.getWriter()) {
            writer.write(json);
            writer.flush();
        }
    }

    private Object parseModelToObject(final Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().stream().findFirst().get();
        }

        return model;
    }
}
