package com.interface21.webmvc.servlet.view;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    private static final int ONE_MODEL_SIZE = 1;
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String UTF_8_ENCODING = "UTF-8";

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String jsonFormattedData = objectMapper.writeValueAsString(getRawData(model));
        response.getWriter().write(jsonFormattedData);
        response.setContentType(JSON_CONTENT_TYPE);
        response.setCharacterEncoding(UTF_8_ENCODING);
    }

    private Object getRawData(final Map<String, ?> model) {
        if (model.size() == ONE_MODEL_SIZE) {
            return model.values().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("모델이 비어있습니다. 데이터를 확인해주세요."));
        }

        return model;
    }
}
