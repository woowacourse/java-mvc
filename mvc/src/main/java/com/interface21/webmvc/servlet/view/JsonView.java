package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    // ObjectMapper는 생성비용이 비싸기 때문에 static 으로 단일 공유
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = convertModelToJson(model);
        response.getWriter().write(json);
    }

    private String convertModelToJson(Map<String, ?> model) {
        try {
            if (model.size() == 1) {
                Object singleValue = model.values().iterator().next();
                return objectMapper.writeValueAsString(singleValue);
            }
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("json 응답 생성에 실패하였습니다.");
        }
    }
}
