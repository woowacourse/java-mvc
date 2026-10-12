package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ObjectMapper objectMapper;

    public JsonView() {
        this(OBJECT_MAPPER);
    }

    public JsonView(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        Object value = selectSerializationTarget(model);

        String json = objectMapper.writeValueAsString(value);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        response.getWriter()
                .write(json);
    }

    private static Object selectSerializationTarget(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values()
                    .iterator()
                    .next();
        }
        return model;
    }
}
