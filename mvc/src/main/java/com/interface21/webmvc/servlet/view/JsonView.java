package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final int SINGLE_DATA_SIZE = 1;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(
            Map<String, ?> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = convertToJson(model);
        response.getWriter().write(json);
    }

    private String convertToJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == SINGLE_DATA_SIZE) {
            Object object = model.values().stream()
                    .findFirst()
                    .get();
            return objectMapper.writeValueAsString(object);
        }

        return objectMapper.writeValueAsString(model);
    }
}
