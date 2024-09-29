package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        Set<String> keySet = model.keySet();
        if (keySet.size() < 2) {
            Object value = model.get(keySet.iterator().next());
            write(response, value);
            return;
        }

        write(response, model);
    }

    private void write(HttpServletResponse response, Object value) throws IOException {
        String json = objectMapper.writeValueAsString(value);
        response.getWriter().write(json);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JsonView jsonView = (JsonView) o;
        return Objects.equals(objectMapper, jsonView.objectMapper);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(objectMapper);
    }
}
