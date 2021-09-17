package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(
        Map<String, ?> model, HttpServletRequest request, HttpServletResponse response
    ) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model.size() == 1) {
            writeJson(model.values().toArray()[0], response);
            return;
        }

        writeJson(model, response);
    }

    private void writeJson(Object value, HttpServletResponse response) throws IOException {
        response.getWriter().write(
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value)
        );
    }
}
