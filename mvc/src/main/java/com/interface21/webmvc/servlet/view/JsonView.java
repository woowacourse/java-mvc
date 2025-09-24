package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
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
            return;
        }
        String content = convertJson(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        writer.print(content);
    }

    private boolean isEmptyModel(Map<String, ?> model) {
        return model == null || model.isEmpty();
    }

    private String convertJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            return "";
        } else if (model.size() == 1) {
            Object value = model.values().stream().findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("비어있는 모델의 값을 JSON으로 변환하려고 하고 있습니다."));
            return objectMapper.writeValueAsString(value);
        }
        return objectMapper.writeValueAsString(model);
    }
}
