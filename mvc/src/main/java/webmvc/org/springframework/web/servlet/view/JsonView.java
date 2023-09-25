package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;
import java.util.Set;

import static web.org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = objectMapper.writeValueAsString(parseModel(model));

        response.getWriter().write(body);
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
    }

    private Object parseModel(Map<String, ?> model) {
        Set<String> keys = model.keySet();
        if (keys.size() == 1) {
            String key = keys.iterator().next();
            return model.get(key);
        }
        return model;
    }
}
