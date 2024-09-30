package com.interface21.webmvc.servlet.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    private static final int PLAINT_TEXT_REND_THRESHOLD = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (model.size() == PLAINT_TEXT_REND_THRESHOLD) {
            renderPlaintext(model, response);
            return;
        }
        response.getWriter().write(json);
    }

    private void renderPlaintext(final Map<String, ?> model, final HttpServletResponse response) throws IOException {
        final List<String> values = model.values()
                .stream()
                .map(String.class::cast)
                .toList();
        for (String value : values) {
            response.getWriter().write(value);
        }
    }
}
