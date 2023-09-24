package webmvc.org.springframework.web.servlet.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import web.org.springframework.http.MediaType;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final var writer = response.getWriter();
        if (model.size() == 1) {
            writeSingleValue(writer, model);
            return;
        }
        writeMultiValue(writer, model);
    }

    private void writeMultiValue(final PrintWriter writer, final Map<String, ?> model) throws JsonProcessingException {
        writer.write(objectMapper.writeValueAsString(model));
    }

    private void writeSingleValue(final PrintWriter writer, final Map<String, ?> model) {
        final var entry = model.entrySet().iterator().next();
        writer.write(entry.getValue().toString());
    }


    @Override
    public String getViewName() {
        return null;
    }
}
