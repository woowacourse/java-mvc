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

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final String content = parseModelToString(model);
        final PrintWriter writer = response.getWriter();
        writer.flush();
        writer.write(content);
    }

    private String parseModelToString(final Map<String, ?> model) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        if (model.size() == 1) {
            return parseSingleValueToString(model, objectMapper);
        }
        return objectMapper.writeValueAsString(model);
    }

    private String parseSingleValueToString(final Map<String, ?> model, final ObjectMapper objectMapper) throws JsonProcessingException {
        final Object next = model.values()
                .iterator()
                .next();
        return objectMapper.writeValueAsString(next);
    }
}
