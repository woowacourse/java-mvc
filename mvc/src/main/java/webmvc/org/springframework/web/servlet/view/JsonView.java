package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int DEFAULT_RESPONSE_MODEL_SIZE = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (model.size() == DEFAULT_RESPONSE_MODEL_SIZE) {
            Object body = model.values().toArray()[0];
            response.getWriter().write(String.valueOf(body));
            return;
        }
        String body = objectMapper.writeValueAsString(model);
        response.setContentLength(body.getBytes(StandardCharsets.UTF_8).length);
        response.getWriter().write(body);
    }
}
