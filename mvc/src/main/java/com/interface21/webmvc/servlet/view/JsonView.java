package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String body = convertModelToJson(model);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        writeResponseBody(response, body);
    }

    private String convertModelToJson(final Map<String, ?> model) throws JsonProcessingException {
        if (isSingleValue(model)) {
            Object singleValue = model.values().iterator().next();
            return objectMapper.writeValueAsString(singleValue);
        }
        return objectMapper.writeValueAsString(model);
    }

    private boolean isSingleValue(final Map<String, ?> model) {
        return model.size() == 1;
    }

    private void writeResponseBody(HttpServletResponse response, String body) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(body);
    }
}
