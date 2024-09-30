package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final int SINGLE_MODEL_SIZE = 1;

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String result = objectMapper.writeValueAsString(extractModel(model));
        response.getWriter().write(result);
    }

    private Object extractModel(Map<String, ?> model) {
        if (model.size() == SINGLE_MODEL_SIZE) {
            return model.values().iterator().next();
        }
        return model;
    }
}
