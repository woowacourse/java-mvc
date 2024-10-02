package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private final JsonMapper jsonMapper;

    public JsonView() {
        this.jsonMapper = new JsonMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model.size() == 1) {
            String singleValue = model.values().iterator().next().toString();
            response.getWriter().write(singleValue);
            return;
        }
        
        String json = jsonMapper.writeValueAsString(model);
        response.getWriter().write(json);
    }
}
