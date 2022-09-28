package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import nextstep.mvc.exception.ViewException;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
                       final HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

        writeData(response, convertToJsonString(model));
    }

    private String convertToJsonString(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (final JsonProcessingException e) {
            throw new ViewException("Failed to serialize a object", e);
        }
    }

    private void writeData(final HttpServletResponse response, final String value) {
        try {
            response.getWriter().write(value);
        } catch (final IOException | NullPointerException e) {
            throw new ViewException("Failed to write data to response.", e);
        }
    }
}
