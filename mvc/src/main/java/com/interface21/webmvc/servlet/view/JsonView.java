package com.interface21.webmvc.servlet.view;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    private static final int PLAINT_TEXT_REND_THRESHOLD = 1;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (model.size() == PLAINT_TEXT_REND_THRESHOLD) {
            renderSingle(model, response);
            return;
        }
        final String json = mapper.writeValueAsString(model);
        response.getWriter().write(json);
    }

    private void renderSingle(final Map<String, ?> model, final HttpServletResponse response) throws IOException {
        final Object value = model.values()
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("모델을 찾을 수 없습니다."));
        final String json = mapper.writeValueAsString(value);
        response.getWriter().write(json);
    }
}
