package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        writeBody(model, response);
    }

    private void writeBody(
            final Map<String, ?> model,
            HttpServletResponse response
    ) throws IOException {
        PrintWriter writer = response.getWriter();

        if (model.size() == 1) {
            Object data = findFirstData(model);
            writer.write(OBJECT_MAPPER.writeValueAsString(data));
        } else {
            writer.write(OBJECT_MAPPER.writeValueAsString(model));
        }
    }

    private Object findFirstData(final Map<String, ?> model) {
        return model.values()
                .iterator()
                .next();
    }
}
