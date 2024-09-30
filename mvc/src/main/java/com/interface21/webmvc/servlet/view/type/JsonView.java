package com.interface21.webmvc.servlet.view.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) {
        int modelSize = model.size();
        if(modelSize < 1) {
            return;
        }
        writeResponse(model, response);
    }

    private void writeResponse(Map<String, ?> model, HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            writer.println(objectMapper.writeValueAsString(model));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
