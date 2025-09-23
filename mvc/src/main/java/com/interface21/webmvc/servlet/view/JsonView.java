package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

public class JsonView implements View {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        ServletOutputStream outputStream = response.getOutputStream();
        writeJsonValues(model, outputStream);
    }

    private void writeJsonValues(Map<String, ?> model, OutputStream outputStream) throws IOException {
        if (model.size() == 1) {
            // model이 하나인 경우에는 그 model의 값만 보여준다.
            Collection<?> values = model.values();
            objectMapper.writeValue(outputStream, values.iterator().next());
            return;
        }
        objectMapper.writeValue(outputStream, model);
    }
}
