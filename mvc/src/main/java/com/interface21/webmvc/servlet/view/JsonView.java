package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Map.Entry;
import jdk.jfr.ContentType;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if(model.size()!=1){
            response.setContentType("application/json");
            for (Entry<String, ?> entry : model.entrySet()) {
                response.getWriter().println(objectMapper.writeValueAsString(entry.getValue()));
            }
            return;
        }
        for (Entry<String, ?> entry : model.entrySet()) {
            response.getWriter().println(entry.getValue());
        }
    }
}
