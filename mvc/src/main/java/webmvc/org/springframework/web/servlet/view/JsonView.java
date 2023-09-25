package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import web.org.springframework.http.MediaType;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (model == null || model.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final PrintWriter writer = response.getWriter();
        if (model.size() == 1) {
            final Object value = model.values().toArray()[0];
            writer.write(String.valueOf(value));
            return;
        }
        writer.write(objectMapper.writeValueAsString(model));
    }
}
