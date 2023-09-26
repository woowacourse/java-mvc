package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

import static web.org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String value = getJsonValue(model);

        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(value);
    }

    private static String getJsonValue(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return objectMapper.writeValueAsString(model.values().toArray()[0]);
        }

        return objectMapper.writeValueAsString(model);
    }
}
