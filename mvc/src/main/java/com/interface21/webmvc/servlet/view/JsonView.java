package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

public class JsonView implements View {

    private static final int UNIQUE_SIZE = 1;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PrintWriter writer = response.getWriter();
        if (model.size() == UNIQUE_SIZE) {
            model.forEach((key, value) -> writer.print(value.toString()));
            return;
        }
        responseJson(model, response, writer);
    }

    private void responseJson(Map<String, ?> model, HttpServletResponse response, Writer writer)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        mapper.writeValue(writer, model);
    }
}
