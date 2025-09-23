package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonView implements View {

    public static JsonView init() {
        return new JsonView();
    }

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);

        final ObjectMapper mapper = new ObjectMapper();

        final Object target = (model.size() == 1)
                ? model.values().iterator().next() // 값 하나면 Value
                : model;                           // 여러 개면 Json

        final String json = mapper.writeValueAsString(target);

        try (final PrintWriter writer = response.getWriter()) {
            writer.write(json);
            writer.flush();
        }
    }
}
