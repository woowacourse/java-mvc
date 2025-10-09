package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final Object payload = getPayloadFrom(model);
        OBJECT_MAPPER.writeValue(response.getWriter(), payload);
    }

    private Object getPayloadFrom(Map<String, ?> model) {
        if (model == null || model.isEmpty()) {
            return null;
        }
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }
}
