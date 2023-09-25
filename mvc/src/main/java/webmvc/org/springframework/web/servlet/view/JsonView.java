package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(objectMapper.getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final String jsonString = writeModelToJsonString(model);
        writeResponseBody(response, jsonString);
    }

    private String writeModelToJsonString(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            final Object value = model.values().iterator().next();
            return objectMapper.writeValueAsString(value);
        }
        return objectMapper.writeValueAsString(model);
    }

    private void writeResponseBody(final HttpServletResponse response, final String body) throws IOException {
        final PrintWriter out = response.getWriter();
        out.write(body);
        out.flush();
    }
}
