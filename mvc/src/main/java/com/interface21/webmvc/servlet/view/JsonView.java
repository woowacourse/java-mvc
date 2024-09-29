package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        if (model.size() == 1) {
            for (Object value : model.values()) {
                writer.write(objectMapper.writeValueAsString(value));
            }
        }
        if (model.size() > 1) {
            writer.write(objectMapper.writeValueAsString(model));
        }
    }
}
