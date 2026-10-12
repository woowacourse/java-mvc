package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonView() {
    }
    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        // 모델의 크기가 2이상인 경우 모델을 직렬화함
        Object data = model;

        // 모델의 크기가 1인 경우 값을 직렬화함
        if (model.size() == 1) {
            data = model.values().iterator().next();
        }

        String json = objectMapper.writeValueAsString(data);
        response.getWriter().write(json);
    }
}
