package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(model);

        response.getWriter().write(body);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
