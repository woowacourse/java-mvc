package com.interface21.webmvc.servlet.view;

import java.io.IOException;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");

        if (model.size() == 1) {
            Object singleValue = model.values().iterator().next();
            responseSingleValue(response, singleValue);
            return;
        }
        objectMapper.writeValue(response.getWriter(), model);
    }

    private void responseSingleValue(HttpServletResponse response, Object singleValue) throws IOException {
        if (singleValue instanceof String) {
            response.getWriter().write(singleValue.toString());
            return;
        }
        objectMapper.writeValue(response.getWriter(), singleValue);
    }
}
