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
        objectMapper.writerWithDefaultPrettyPrinter();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String value = writeJsonData(model);
        final PrintWriter writer = response.getWriter();

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        writer.write(value);
        writer.flush();
    }

    private String writeJsonData(final Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            return "";
        }
        if (model.size() == 1) {
            return objectMapper.writeValueAsString(getFirstData(model));
        }
        return objectMapper.writeValueAsString(model);
    }

    private Object getFirstData(final Map<String, ?> model) {
        return model.keySet()
                .stream()
                .map(model::get)
                .findFirst()
                .orElseThrow();
    }
}
