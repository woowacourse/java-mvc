package com.interface21.webmvc.servlet.view;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    private final ObjectMapper mapper;

    public JsonView() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final Writer writer = response.getWriter();

        if (hasOnlyOneKey(model)) {
            writer.write(mapper.writeValueAsString(model.values().iterator().next()));
            return;
        }
        writer.write(mapper.writeValueAsString(model));
    }

    private boolean hasOnlyOneKey(final Map<String, ?> model) {
        return model.keySet().size() == 1;
    }
}
