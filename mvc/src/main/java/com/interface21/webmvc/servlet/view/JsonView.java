package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void render(Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = convertToJson(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        response.getWriter().write(json);
        response.getWriter().flush();
    }

    private String convertToJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            Map.Entry<String, ?> firstEntry = model.entrySet().iterator().next();
            return objectMapper.writeValueAsString(firstEntry.getValue());
        }

        return objectMapper.writeValueAsString(model);
    }
}
