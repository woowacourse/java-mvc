package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String ERROR_JSON = "{\"error\":\"Internal server error\"}";

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        response.setContentType(CONTENT_TYPE);

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        log.debug("Rendering JSON view with model: {}", model);

        try (PrintWriter writer = response.getWriter()) {
            String jsonResponse = getJsonResponse(model);
            writer.print(jsonResponse);

            writer.flush();
        } catch (Exception e) {
            log.error("Error rendering JSON response", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            try (PrintWriter writer = response.getWriter()) {
                writer.print(ERROR_JSON);

                writer.flush();
            }
        }
    }

    private String getJsonResponse(Map<String, ?> model) throws Exception {
        if (model == null || model.isEmpty()) {
            return "{}";
        }

        return objectMapper.writeValueAsString(model);
    }
}
