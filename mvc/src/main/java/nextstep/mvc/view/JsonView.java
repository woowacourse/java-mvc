package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

public class JsonView implements View {

    private static final int IS_SINGLE_VALUE = 1;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writeValue(model, writer);
    }

    private void writeValue(Map<String, ?> model, PrintWriter writer) throws IOException {
        if (model.size() == IS_SINGLE_VALUE) {
            Collection<?> values = model.values();
            objectMapper.writeValue(writer, values.toArray());
            return;
        }
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, model);
    }
}
