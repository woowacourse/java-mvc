package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Object result = extractResult(model);
        String json = toJson(result);
        writeResponse(response, json);
    }

    private Object extractResult(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }

    private String toJson(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            log.debug("JSON response: {}", json);
            return json;
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to JSON: {}", object, e);
            throw new IllegalStateException("JSON serialization failed", e);
        }
    }

    private void writeResponse(HttpServletResponse response, String json) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(json);
        }
    }
}
