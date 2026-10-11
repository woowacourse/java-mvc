package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        // JSON 응답임을 클라이언트에게 알린다.
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        // model이 하나라면 Map 껍데기 없이 값 자체를 JSON으로 반환한다.
        final Object value = getValue(model);

        final String json = objectMapper.writeValueAsString(value);

        response.getWriter().write(json);
    }

    private Object getValue(final Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values()
                    .iterator()
                    .next();
        }

        // 데이터가 없거나 2개 이상이면 Map 자체를 JSON으로 변환한다.
        return model;
    }
}