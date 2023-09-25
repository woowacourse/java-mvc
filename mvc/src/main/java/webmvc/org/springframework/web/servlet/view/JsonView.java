package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final String jsonResponse = convertToJsonResponse(model);
        final PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
    }

    private String convertToJsonResponse(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            final var value = model.values()
                    .iterator()
                    .next();
            return String.valueOf(value);
        }
        return objectMapper.writeValueAsString(model);
    }
}
