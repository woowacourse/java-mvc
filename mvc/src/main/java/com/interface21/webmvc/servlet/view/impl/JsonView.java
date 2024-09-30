package com.interface21.webmvc.servlet.view.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.view.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private static final String MEDIA_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType(MEDIA_TYPE);
            String json = getJson(model);
            response.getWriter().write(json);
        } catch (IOException ex) {
            throw new JsonViewRenderFailedException(ex);
        }
    }

    private String getJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            Object singleValue = model.values().iterator().next();
            return objectMapper.writeValueAsString(singleValue);
        }
        return objectMapper.writeValueAsString(model);
    }
}
