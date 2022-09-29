package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (model.size() == 1) {
            final var value = model.values().stream()
                    .findFirst()
                    .get();
            writeResponse(response, value);
            return;
        }
        writeResponse(response, model);
    }

    private void writeResponse(final HttpServletResponse response, final Object object) throws IOException {
        final var jsonValue = objectMapper.writeValueAsString(object);
        final var writer = response.getWriter();
        writer.write(jsonValue);
    }
}
