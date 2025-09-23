package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonResponse = objectMapper.writeValueAsString(extractModel(model));

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    private Object extractModel(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().toArray()[0];
        }
        return model;
    }
}
