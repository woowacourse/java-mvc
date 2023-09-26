package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;
import java.util.Set;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String body = findBody(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);
    }

    private String findBody(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            final Set<String> keys = model.keySet();
            final String firstKey = (String) keys.toArray()[0];

            return objectMapper.writeValueAsString(model.get(firstKey));
        }

        return objectMapper.writeValueAsString(model);
    }
}
