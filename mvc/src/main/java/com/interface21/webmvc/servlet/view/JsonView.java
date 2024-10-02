package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int SINGLE_MODEL_SIZE = 1;
    private static final int SINGLE_MODEL_INDEX = 0;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        checkModelIsEmpty(model);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final PrintWriter writer = response.getWriter();
        if (model.size() == SINGLE_MODEL_SIZE) {
            writeSingleModel(model, writer);
            return;
        }
        writer.write(objectMapper.writeValueAsString(model));
    }

    private void checkModelIsEmpty(Map<String, ?> model) {
        if (model.isEmpty()) {
            throw new IllegalStateException("렌더링할 모델이 존재하지 않습니다. ");
        }
    }

    private void writeSingleModel(Map<String, ?> model, PrintWriter writer) {
        final Object value = model.values().toArray()[SINGLE_MODEL_INDEX];
        writer.write(String.valueOf(value));
    }

    @Override
    public String getName() {
        return "json";
    }
}
