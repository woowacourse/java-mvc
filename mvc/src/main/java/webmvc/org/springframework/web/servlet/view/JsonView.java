package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.exception.JsonViewParseException;

public class JsonView implements View {

    private static final String JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        final String responseBody = convertModelToResponseBody(model);
        response.setContentType(JSON_MEDIA_TYPE);
        response.getWriter().write(responseBody);
    }

    private String convertModelToResponseBody(final Map<String, ?> model) {
        if (model.size() == 1) {
            final Object body = model.values()
                .iterator()
                .next();
            return String.valueOf(body);
        }
        try {
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new JsonViewParseException();
        }
    }
}
