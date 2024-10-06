package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private static final int FIRST_VALUE_INDEX = 0;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

            if (model.size() == 1) {
                Object value = model.values().toArray()[FIRST_VALUE_INDEX];
                response.getWriter().write(objectMapper.writeValueAsString(value));
                return;
            }
            response.getWriter().write(objectMapper.writeValueAsString(model));
        } catch (IOException e) {
            throw new RuntimeException("failed to write value into JSON", e);
        }
    }
}
