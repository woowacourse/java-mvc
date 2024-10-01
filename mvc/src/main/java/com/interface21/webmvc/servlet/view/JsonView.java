package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Integer SINGLE_SIZE = 1;

    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(convertToJsonFormat(model));
    }

    private String convertToJsonFormat(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == SINGLE_SIZE) {
            Object data = model.values().iterator().next();
            return objectMapper.writeValueAsString(data);
        }
        return objectMapper.writeValueAsString(model);
    }
}
