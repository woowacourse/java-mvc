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

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // JSON 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE로 반환
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        response.getWriter().write(toJsonString(model));
    }

    private String toJsonString(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            // model에 데이터가 1개인 경우
            Object singleValue = model.values().iterator().next();
            return objectMapper.writeValueAsString(singleValue);
        }
        // model에 데이터가 2개 이상인 경우: Map 형태 그대로
        return objectMapper.writeValueAsString(model);
    }
}
