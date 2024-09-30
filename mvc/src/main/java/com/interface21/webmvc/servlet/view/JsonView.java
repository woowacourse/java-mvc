package com.interface21.webmvc.servlet.view;

import static com.interface21.web.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = parseBody(model);
        setBody(response, body);
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
    }

    private static String parseBody(Map<String, ?> model) throws JsonProcessingException {
        StringBuilder body = new StringBuilder();
        if (model.size() == 1) {
            Object value = model.values().iterator().next();
            body.append(new ObjectMapper().writeValueAsString(value));
            return body.toString();
        }
        body.append(new ObjectMapper().writeValueAsString(model));
        return body.toString();
    }

    private void setBody(HttpServletResponse response, String body) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(body);
    }
}
