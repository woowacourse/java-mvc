package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final PrintWriter writer = response.getWriter();
        final String body = convertString(model);
        writer.write(body);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private String convertString(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            final var value = model.values().toArray()[0];
            return objectMapper.writeValueAsString(value);
        }
        final String body = objectMapper.writeValueAsString(model);
        return body;
    }

    @Override
    public boolean isRedirectView() {
        return false;
    }

    @Override
    public String getViewName() {
        return null;
    }

}
