package com.interface21.webmvc.servlet.view;

import java.io.PrintWriter;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        String result = objectMapper.writeValueAsString(getTargetData(model));

        writer.write(result);
    }

    private Object getTargetData(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }

        return model;
    }
}
