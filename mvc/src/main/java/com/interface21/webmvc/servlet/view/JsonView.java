package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {
    private static final String JSON_CONTENT_TYPE = "Application/json";

    private static final String EMPTY_STRING = "";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    static {
        OBJECT_MAPPER.setVisibility(
                OBJECT_MAPPER.getSerializationConfig()
                        .getDefaultVisibilityChecker()
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String responseBody = createResponseBody(model);

        response.setContentType(JSON_CONTENT_TYPE);
        setResponseBody(response, responseBody);
    }

    private String createResponseBody(Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            return EMPTY_STRING;
        }
        if (model.size() == 1) {
            Object object = model.values().iterator().next();
            log.debug("object = {}", object);
            return OBJECT_MAPPER.writeValueAsString(object);
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }

    private void setResponseBody(HttpServletResponse response, String body) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(body);
        writer.flush();
    }
}
