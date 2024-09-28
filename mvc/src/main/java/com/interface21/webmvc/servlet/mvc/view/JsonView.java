package com.interface21.webmvc.servlet.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.mvc.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = getJson(model);
        response.getWriter().write(json);
    }

    private String getJson(Map<String, ?> model) throws JsonProcessingException {
        if (hasSingleSize(model)) {
            Object singleValue = model.values().iterator().next();
            return objectMapper.writeValueAsString(singleValue);
        }
        return objectMapper.writeValueAsString(model);
    }

    private boolean hasSingleSize(Map<String, ?> model) {
        return model.size() == 1;
    }
}
