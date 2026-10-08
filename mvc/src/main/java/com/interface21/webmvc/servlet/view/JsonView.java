package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger logger = LoggerFactory.getLogger(JsonView.class);
    private static final String JSON_TYPE = "application/json;charset=UTF-8";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String responseBody = objectMapper.writeValueAsString(model);
        logger.debug("written = {}", responseBody);

        response.setContentType(JSON_TYPE);
        response.getWriter().write(responseBody);
    }
}
