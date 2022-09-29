package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

public class JsonView implements View {

    private static final int MIN_MODEL_SIZE = 1;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String responseBody = parseResponseBody(model);

        response.setContentType("MediaType.APPLICATION_JSON_UTF8_VALUE");
        PrintWriter writer = response.getWriter();
        writer.write(responseBody);
    }

    private String parseResponseBody(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == MIN_MODEL_SIZE) {
            Collection<?> values = model.values();
            return OBJECT_MAPPER.writeValueAsString(values.toArray()[0]);
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
