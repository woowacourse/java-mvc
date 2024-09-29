package com.interface21.webmvc.servlet.view;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    private static final int SINGLE_ITEM_MODEL_SIZE = 1;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        String json = getResponseJson(model);
        response.getWriter().print(json);
    }

    private String getResponseJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == SINGLE_ITEM_MODEL_SIZE) {
            return objectMapper.writeValueAsString(model.values().iterator().next());
        }
        return objectMapper.writeValueAsString(model);
    }
}
