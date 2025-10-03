package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        if (model.size() == 1) {
            Object[] values = model.values().toArray();
            response.getWriter().write(mapper.writeValueAsString(values[0]));
        } else {
            response.getWriter().write(mapper.writeValueAsString(model));
        }
    }
}
