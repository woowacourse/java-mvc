package webmvc.org.springframework.web.servlet.view;

import static web.org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String responseBody = parseBody(model);
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(responseBody);
    }

    private static String parseBody(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return String.valueOf(model.values().toArray()[0]);
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
