package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = DEFAULT_OBJECT_MAPPER.writeValueAsString(extractModel(model));
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);
    }

    private Object extractModel(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }
}
