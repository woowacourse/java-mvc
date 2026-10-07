package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonView() {
        this(new ObjectMapper());
    }

    public JsonView(ObjectMapper mapper) {
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model.size() == 1) {
            response.getWriter().write(mapper.writeValueAsString(model.values().iterator().next()));
            return;
        }

        String json = mapper.writeValueAsString(model);
        response.getWriter().write(json);
    }
}
