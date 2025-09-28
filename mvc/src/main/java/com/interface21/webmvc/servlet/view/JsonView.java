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

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("JsonView render model : {}", model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        String json = toJson(model);
        response.getWriter().write(json);
    }

    private String toJson(final Map<String, ?> model) throws IOException {
        try {
            if (model.size() == 1) {
                final Object value = model.values().iterator().next();
                return objectMapper.writeValueAsString(value);
            }

            return objectMapper.writeValueAsString(model);
        } catch (final JsonProcessingException e) {
            throw new IOException(e);
        }
    }
}
