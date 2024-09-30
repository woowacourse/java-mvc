package com.interface21.webmvc.servlet.view;

import static com.interface21.web.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final int SINGLE_SIZE = 1;

    private static final ObjectMapper objectMapper = new ObjectMapper();;

    public JsonView() {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = parseBody(model);
        setBody(response, body);
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
    }

    private static String parseBody(Map<String, ?> model) throws JsonProcessingException {
        StringBuilder body = new StringBuilder();
        if (model.size() == SINGLE_SIZE) {
            Object value = model.values().iterator().next();
            body.append(objectMapper.writeValueAsString(value));
            return body.toString();
        }
        body.append(objectMapper.writeValueAsString(model));
        return body.toString();
    }

    private void setBody(HttpServletResponse response, String body) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(body);
    }
}
