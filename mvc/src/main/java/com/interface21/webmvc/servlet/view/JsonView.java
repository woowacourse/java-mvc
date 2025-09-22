package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    // ObjectMapper는 생성비용이 비싸기 때문에 static 으로 단일 공유
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectWriter objectWriter = objectMapper.writer();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        objectWriter.writeValue(response.getOutputStream(), getModelResponse(model));
    }

    private Object getModelResponse(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }
}
