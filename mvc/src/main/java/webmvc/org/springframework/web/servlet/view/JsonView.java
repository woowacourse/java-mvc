package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private static final int SINGLE_MODEL_SIZE = 1;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding("UTF-8");

        if (model.isEmpty()) {
            return;
        }

        if (model.size() == SINGLE_MODEL_SIZE) {
            final Object oneValue = model.values().toArray()[0];
            writeObjectAsJson(response, oneValue);
            return;
        }

        writeObjectAsJson(response, model);
    }

    private void writeObjectAsJson(final HttpServletResponse response, final Object value) throws IOException {
        final String valueAsString = objectMapper.writeValueAsString(value);
        response.getWriter().write(valueAsString);
    }
}
