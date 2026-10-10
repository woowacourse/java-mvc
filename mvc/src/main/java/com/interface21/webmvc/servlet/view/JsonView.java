package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object target = model;
        if (model.size() == 1) {
            target = model.values().iterator().next();
        }

        final String responseBody = objectMapper.writeValueAsString(target);

        logger.debug("written = {}", responseBody);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(responseBody);
    }
}
