package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int SINGLE_ELEMENT_SIZE = 1;

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws IOException {
        setResponseHeaderForJsonView(response);
        writeJsonStringInResponse(model, response);
    }

    private void setResponseHeaderForJsonView(final HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    }

    private void writeJsonStringInResponse(final Map<String, ?> model,
                                           final HttpServletResponse response) throws IOException {
        final String jsonString = getJsonString(model);

        try (final PrintWriter writer = response.getWriter()) {
            writer.write(jsonString);
            writer.flush();
        }
    }

    private String getJsonString(Map<String, ?> model) {
        if (model.size() == SINGLE_ELEMENT_SIZE) {
            final Object[] modelElements = model.values().toArray();
            return writeValueAsString(modelElements[0]);
        }
        return writeValueAsString(model);
    }

    private String writeValueAsString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
