package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonView implements View {

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private final ObjectMapper objectMapper;
    private final Object data;

    public JsonView(Object data) {
        this.data = data;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        String json = objectMapper.writeValueAsString(data);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
