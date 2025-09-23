package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (model == null) {
            return;
        }

        String body;
        if (model.size() == 1) {
            Object singleObject = model.values().stream()
                    .findFirst()
                    .get();
            body = objectMapper.writeValueAsString(singleObject);
        } else {
            body = objectMapper.writeValueAsString(model);
        }

        response.getWriter().write(body);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
