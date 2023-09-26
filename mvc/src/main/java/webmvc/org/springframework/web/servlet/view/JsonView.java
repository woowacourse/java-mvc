package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final PrintWriter writer = response.getWriter();
        if (isSingleValue(model)) {
            writeValuesWhenSingleValue(model, writer);
            return;
        }
        objectMapper.writeValue(response.getWriter(), model);
    }

    private boolean isSingleValue(final Map<String, ?> model) {
        return model.size() == 1;
    }

    private void writeValuesWhenSingleValue(final Map<String, ?> model, final PrintWriter writer) throws IOException {
        for (final Object value : model.values()) {
            objectMapper.writeValue(writer, value);
        }
    }
}
