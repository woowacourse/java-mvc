package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.Objects;

import static com.interface21.web.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public class JsonView implements View {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        final String jsonContent = serialize(model);
        response.getWriter().write(jsonContent);
    }

    private String serialize(final Map<String, ?> model) throws JsonProcessingException {
        if(Objects.isNull(model) || model.isEmpty()) {
            return "";
        }
        if(model.size() == 1) {
            return model.values().iterator().next().toString();
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
