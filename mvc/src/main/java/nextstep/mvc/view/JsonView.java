package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        Writer writer = response.getWriter();
        writeToResponse(writer, model);
    }

    private void writeToResponse(Writer writer, Map<String, Object> model) throws IOException {
        if (model.size() == 1) {
            Map.Entry<String, ?> entry = model.entrySet().iterator().next();
            String key = entry.getKey();
            objectMapper.writeValue(writer, model.get(key));
        }
        if (model.size() > 1) {
            objectMapper.writeValue(writer, model);
        }
    }
}
