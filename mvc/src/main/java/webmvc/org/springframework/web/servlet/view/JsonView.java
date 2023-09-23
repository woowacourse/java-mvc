package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String UTF_8_ENCODING = "utf-8";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(JSON_CONTENT_TYPE);
        response.setCharacterEncoding(UTF_8_ENCODING);
        PrintWriter writer = response.getWriter();

        if (model.size() == 1) {
            Object value = model.values().toArray()[0];
            writer.write(objectMapper.writeValueAsString(value));
            return;
        }
        writer.write(objectMapper.writeValueAsString(model));
    }
}
