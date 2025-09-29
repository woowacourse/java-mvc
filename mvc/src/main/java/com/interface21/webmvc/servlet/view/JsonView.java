package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("MediaType.APPLICATION_JSON_UTF8_VALUE");

        for (String modelName : model.keySet()) {
            Object modelValue = model.get(modelName);
            Class<?> modelClass = modelValue.getClass();

            Field[] fields = modelClass.getFields();

            String output = serialize(fields, modelValue);
            response.getWriter().write(output);
        }
    }

    private String serialize(Field[] fields, Object modelValue) throws Exception {
        if (fields.length == 1) {
            return (String) fields[0].get(modelValue);
        }
        return objectMapper.writeValueAsString(modelValue);
    }

    @Override
    public String getViewName() {
        return "";
    }
}
