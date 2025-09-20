package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

public class JsonView implements View {

    @Override
    public void render(final HttpServletRequest request, HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> attributes = Collections.list(request.getAttributeNames()).stream()
            .map(request::getAttribute)
            .toList();

        ServletOutputStream outputStream = response.getOutputStream();
        objectMapper.writeValue(outputStream, attributes);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Override
    public String getViewName() {
        return "";
    }
}
