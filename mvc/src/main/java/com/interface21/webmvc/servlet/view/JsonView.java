package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        log.debug("Rendering JSON. Model size: {}", model.size());
        model.forEach((key, value) -> log.debug("Model entry: key={}, value.class={}", key, value.getClass().getName()));

        final PrintWriter writer = response.getWriter();
        if (model.size() == 1) {
            Object value = model.values().iterator().next();
            log.debug("Model size is 1. Serializing value: {}", value);
            writer.write(OBJECT_MAPPER.writeValueAsString(value));
            return;
        }

        log.debug("Model size is > 1. Serializing entire model map.");
        writer.write(OBJECT_MAPPER.writeValueAsString(model));
    }
}