package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final int VALID_MODEL_SIZE = 1;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Writer writer = response.getWriter();
        writeToResponse(writer, model);
    }

    private void writeToResponse(Writer writer, Map<String, Object> model) throws IOException {
        if (model.size() == VALID_MODEL_SIZE) {
            Map.Entry<String, ?> entry = model.entrySet().iterator().next();
            String key = entry.getKey();
            objectMapper.writeValue(writer, model.get(key));
        }
        if (model.size() > 1) {
            objectMapper.writeValue(writer, model);
        }
    }
}