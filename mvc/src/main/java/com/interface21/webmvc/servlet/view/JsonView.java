package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
                       final HttpServletResponse response) throws IOException {
        prepareResponse(response);
        final Object value = extractValue(model);
        final String json = objectMapper.writeValueAsString(value); // JAVA 객체 => JSON 문자열로 변환
        response.getWriter().write(json); // 변환된 문자열을 HTTP 응답 본문에 기록
    }

    private void prepareResponse(final HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private Object extractValue(final Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }
}
