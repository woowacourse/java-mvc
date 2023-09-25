package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int DEFAULT_RETURN_SIZE = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jsonValue = MAPPER.writeValueAsString(model);

        if (model.size() == DEFAULT_RETURN_SIZE) {
            response.setContentType(MediaType.TEXT_PLAIN_UTF8_VALUE);
            response.getWriter().write(jsonValue);
            return;
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(jsonValue);
    }
}
