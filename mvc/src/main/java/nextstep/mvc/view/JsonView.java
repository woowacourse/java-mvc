package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final PrintWriter writer = response.getWriter();
        writeContent(model, writer);
    }

    private void writeContent(final Map<String, ?> model, final PrintWriter writer) throws IOException {
        if (model.size() == 1) {
            final Object value = model.values().toArray()[0];
            objectMapper.writeValue(writer, value);
            return;
        }

        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(writer, model);
    }
}
