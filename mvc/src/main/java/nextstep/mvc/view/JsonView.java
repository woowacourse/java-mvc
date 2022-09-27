package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private void writeContent(final Map<String, ?> model, final PrintWriter writer) throws JsonProcessingException {
        if (model.size() == 1) {
            final String key = (String) model.keySet().toArray()[0];
            final Object value = model.get(key);
            writer.write(value.toString());
            return;
        }

        final String value = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(model);
        writer.write(value);
    }
}
