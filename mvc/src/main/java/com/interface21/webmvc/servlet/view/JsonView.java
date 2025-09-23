package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter();
    }

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        String value = writeJsonData(model);
        PrintWriter writer = response.getWriter();

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        writer.write(value);
        writer.flush();
    }

    private String writeJsonData(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return objectMapper.writeValueAsString(getFirstData(model));
        }
        return objectMapper.writeValueAsString(model);
    }

    private Object getFirstData(final Map<String, ?> model) {
        return model.keySet()
                .stream()
                .map(model::get)
                .findFirst()
                .orElseThrow();
    }
}
