package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String KEY_VALUE_DELIMITER = ":";
    private static final String JSON_END_FORMAT = "}";

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        var value = objectMapper.writeValueAsString(model);
        if (isSingleValueWithNoDepth(value)) {
            renderPlainText(response, value);
            return;
        }
        renderJson(response, value);
    }

    private boolean isSingleValueWithNoDepth(String value) {
        return value.split(KEY_VALUE_DELIMITER).length == 2;
    }

    private void renderPlainText(HttpServletResponse response, String modelValue) throws IOException {
        try (final var writer = response.getWriter()) {
            String value = modelValue.split(KEY_VALUE_DELIMITER)[1];
            writer.write(value.split(JSON_END_FORMAT)[0]);
            writer.flush();
        }
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
    }

    private void renderJson(HttpServletResponse response, String value) throws IOException {
        try (final var writer = response.getWriter()) {
            writer.write(value);
            writer.flush();
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
