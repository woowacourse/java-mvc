package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final int SINGLE_OBJECT = 1;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        setContentType(response);
        writeJsonResponse(model, response);
    }

    private void setContentType(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private void writeJsonResponse(final Map<String, ?> model, final HttpServletResponse response) throws Exception {
        try (PrintWriter writer = response.getWriter()) {
            Object responseObject = getResponseObject(model);
            objectMapper.writeValue(writer, responseObject);
        }
    }

    private Object getResponseObject(final Map<String, ?> model) {
        if (model.size() == SINGLE_OBJECT) {
            return model.values().iterator().next();
        }
        return model;
    }
}
