package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        if (isEmptyModel(model)) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        String content = convertJson(model);
        PrintWriter writer = response.getWriter();
        writer.print(content);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private boolean isEmptyModel(Map<String, ?> model) {
        return model == null || model.isEmpty();
    }

    private String convertJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            return "";
        } else if (model.size() == 1) {
            String key = model.keySet().stream().toList().getFirst();
            return objectMapper.writeValueAsString(model.get(key));
        }
        return objectMapper.writeValueAsString(model);
    }
}
