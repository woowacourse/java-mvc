package webmvc.org.springframework.web.servlet.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.mvc.View;

import java.util.Map;

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
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final PrintWriter writer = response.getWriter();

        if (model.isEmpty()) {
            writer.write(BLANK);
            return ;
        }

        if (model.size() == MINIMUM_MODEL_SIZE) {
            final Object responseBody = model.values().toArray(Object[]::new)[0];

            writer.write(objectMapper.writeValueAsString(responseBody));
            return ;
        }

        writer.write(objectMapper.writeValueAsString(model));
    }
}
