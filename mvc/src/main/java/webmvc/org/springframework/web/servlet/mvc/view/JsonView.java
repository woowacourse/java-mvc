package webmvc.org.springframework.web.servlet.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.mvc.View;

public class JsonView implements View {

    private static final String BLANK = "";
    private static final int MINIMUM_MODEL_SIZE = 1;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final String responseBody = findResponseBody(model);

        response.getWriter()
                .write(responseBody);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private String findResponseBody(final Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            return BLANK;
        }
        if (model.size() == MINIMUM_MODEL_SIZE) {
            final Object responseBody = model.values().toArray(Object[]::new)[0];

            return objectMapper.writeValueAsString(responseBody);
        }
        return objectMapper.writeValueAsString(model);
    }
}
