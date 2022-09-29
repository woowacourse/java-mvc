package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        if (model.size() == 1) {
            writeValue(model, writer);
            return;
        }
        writer.print(OBJECT_MAPPER.writeValueAsString(model));
    }

    private void writeValue(Map<String, ?> model, PrintWriter writer) throws JsonProcessingException {
        for (Object value : model.values()) {
            writer.print(OBJECT_MAPPER.writeValueAsString(value));
        }
    }
}
