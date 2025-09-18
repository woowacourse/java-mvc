package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = toJson(model);
        //PrintWriter를 가져옴. **텍스트 데이터**를 응답 본문에 쓸 수 있게 해줌
        response.getWriter().write(json);
    }

    private String toJson(final Map<String, ?> model) throws JsonProcessingException {
        Object objectToSerialize;
        if (model.size() == 1) {
            objectToSerialize = model.values().iterator().next();
        } else {
            objectToSerialize = model;
        }
        return objectMapper.writeValueAsString(objectToSerialize);
    }
}
