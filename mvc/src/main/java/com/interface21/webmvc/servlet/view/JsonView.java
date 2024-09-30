package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private static final int ONE_MODEL_SIZE = 1;
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String UTF_8_ENCODING = "UTF-8";

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(JSON_CONTENT_TYPE);
        response.setCharacterEncoding(UTF_8_ENCODING);

        if (model.size() == ONE_MODEL_SIZE) {
            String rawData = model.values().iterator().next().toString();
            response.getWriter().write(rawData);
            return;
        }

        String jsonFormattedData = objectMapper.writeValueAsString(model);
        response.getWriter().write(jsonFormattedData);
    }
}
