package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final int UNIQUE_SIZE = 1;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PrintWriter writer = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Object content = getContent(model);
        mapper.writeValue(writer, content);
    }

    private Object getContent(Map<String, ?> model) {
        if (model.size() == UNIQUE_SIZE) {
            return model.values().iterator().next();
        }
        return model;
    }
}
