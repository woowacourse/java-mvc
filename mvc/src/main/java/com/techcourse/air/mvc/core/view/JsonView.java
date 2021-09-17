package com.techcourse.air.mvc.core.view;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techcourse.air.mvc.web.support.MediaType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(jsonData(model));
    }

    private String jsonData(Map<String, ?> model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        if (model.size() == 1) {
            return objectMapper.writeValueAsString(firstData(model));
        }
        return objectMapper.writeValueAsString(model);
    }

    private Object firstData(Map<String, ?> model) {
        return model.values().toArray()[0];
    }
}
