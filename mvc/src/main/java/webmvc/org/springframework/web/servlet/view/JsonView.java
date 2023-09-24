package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model.size() == 1) {
            final List<?> objects = new ArrayList<>(model.values());
            final Object object = objects.get(0);
            writeResponseBody(response, object);
            return;
        }
        writeResponseBody(response, model);
    }

    private void writeResponseBody(final HttpServletResponse response, final Object object) throws IOException {
        final PrintWriter printWriter = response.getWriter();
        final String body = objectMapper.writeValueAsString(object);
        printWriter.write(body);
    }
}
