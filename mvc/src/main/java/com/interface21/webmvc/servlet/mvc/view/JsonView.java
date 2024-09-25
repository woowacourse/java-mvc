package com.interface21.webmvc.servlet.mvc.view;

import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = createJson(model);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(json);
    }

    private String createJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            Object value = model.values().iterator().next();
            return objectMapper.writeValueAsString(value);
        }

        return objectMapper.writeValueAsString(model);
    }
}
