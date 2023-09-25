package webmvc.org.springframework.web.servlet.view;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static web.org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (model == null || model.isEmpty()) {
            response.setStatus(SC_NOT_FOUND);
            return;
        }
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        final String body = objectMapper.writeValueAsString(model);
        response.getWriter().write(body);
    }
}
