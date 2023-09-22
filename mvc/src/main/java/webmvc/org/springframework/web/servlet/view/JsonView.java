package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final int ORIGINAL_VALUE_RETURN_SIZE = 1;

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String value = objectMapper.writeValueAsString(model);

        if (model.size() == ORIGINAL_VALUE_RETURN_SIZE) {
            response.setContentType(MediaType.TEXT_PLAIN_UTF8_VALUE);
            response.getWriter().write(value);
            return;
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(value);
    }
}
