package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    // ObjectMapper는 생성 비용이 크고 설정 후에는 thread-safe 하므로 하나만 만들어 재사용한다
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final String json = OBJECT_MAPPER.writeValueAsString(toRenderTarget(model));
        response.getWriter().write(json);
    }

    // model에 데이터가 1개면 값만 꺼내서, 2개 이상이면 Map 그대로 JSON으로 만든다
    private Object toRenderTarget(final Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }
}
