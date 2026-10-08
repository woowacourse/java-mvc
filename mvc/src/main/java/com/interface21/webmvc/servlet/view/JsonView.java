package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {
    // json 변환 도구 객체
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object data = model;

        if (model.size() == 1) {
            // 데이터가 한 개일 경우 값만 추출
            data = model.values().iterator().next();
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        String json = objectMapper.writeValueAsString(data);
        response.getWriter().write(json);
    }
}
