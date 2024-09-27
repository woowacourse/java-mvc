package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Writer;
import java.util.Map;

public class JsonView implements View {

    private static final int FLAT_MODEL_SIZE = 1;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Object rawResponseBody = getResponseFromModel(model);
        Writer responseWriter = response.getWriter();

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        MAPPER.writeValue(responseWriter, rawResponseBody);
    }

    private Object getResponseFromModel(Map<String, ?> model) {
        if (needsFlattening(model)) {
            return model.values().iterator().next();
        }
        return model;
    }

    private boolean needsFlattening(Map<String, ?> model) {
        return model.size() == FLAT_MODEL_SIZE;
    }
}
