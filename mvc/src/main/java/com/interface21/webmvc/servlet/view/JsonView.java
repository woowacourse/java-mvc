package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {

        for (String key : model.keySet()) {
            byte[] bytes = objectMapper.writeValueAsBytes(model.get(key));
            response.getOutputStream()
                    .write(bytes);
        }

        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
