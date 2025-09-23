package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ServletOutputStream outputStream = response.getOutputStream();

        ArrayList<String> attributeNames = Collections.list(request.getAttributeNames());
        if (attributeNames.size() == 1) {
            String name = attributeNames.getFirst();
            Object attribute = request.getAttribute(name);

            objectMapper.writeValue(outputStream, attribute);
            return;
        }

        Map<String, Object> attributes = attributeNames.stream()
            .collect(Collectors.toMap(
                name -> name,
                request::getAttribute
            ));
        objectMapper.writeValue(outputStream, attributes);
    }

    @Override
    public String getViewName() {
        return "";
    }
}
