package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object object = model.size() == 1 ? model.values().iterator().next() : model;
        String string = objectMapper.writeValueAsString(object);
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        outputStream.print(string);
    }
}
