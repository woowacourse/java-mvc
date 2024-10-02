package com.interface21.webmvc.servlet.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JsonView implements View {

    private static final JsonMapper jsonMapper = new JsonMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object value = getValue(model);
        String json = jsonMapper.writeValueAsString(value);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        write(response, json);

    }

    private Object getValue(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }

    private void write(HttpServletResponse response, String json) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }
}
