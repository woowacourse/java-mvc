package com.interface21.webmvc.servlet.view;

import static org.reflections.Reflections.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
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

        model.forEach((key, value) -> {
            log.debug("attribute name : {}, value : {}", key, value);
            request.setAttribute(key, value);
        });

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);

        try (PrintWriter writer = response.getWriter()) {
            write(writer, model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(PrintWriter writer, final Map<String, ?> model) throws IOException {
        if (model.size() == 1) {
            objectMapper.writeValue(writer, getFirst(model));
            return;
        }
        objectMapper.writeValue(writer, model);
    }

    private Object getFirst(final Map<String, ?> model) {
        return model.keySet()
                .stream()
                .map(model::get)
                .findFirst()
                .orElseThrow();
    }
}
