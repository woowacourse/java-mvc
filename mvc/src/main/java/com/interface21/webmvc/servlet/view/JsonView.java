package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final int NON_ARRAY_MODEL_SIZE = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        final var json = parseToJson(model);
        log.debug("json : {}", json);
        response.getWriter().write(json);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private String parseToJson(final Map<String, ?> model) throws JsonProcessingException {
        final var objectMapper = new ObjectMapper();
        if (model.size() == NON_ARRAY_MODEL_SIZE) {
            final var value = model.values().iterator().next();
            return objectMapper.writeValueAsString(value);
        }
        return objectMapper.writeValueAsString(model);
    }
}
