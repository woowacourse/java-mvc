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
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String value = objectMapper.writeValueAsString(model);

        if (model.size() == ORIGINAL_VALUE_RETURN_SIZE) {
            Object originalValue = model.values().toArray()[0];
            response.getWriter().write(String.valueOf(originalValue));
            return;
        }

        response.getWriter().write(value);
    }
}
