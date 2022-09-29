package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (hasOneModel(model)) {
            final Object object = extractFirstValue(model);
            writeBody(objectMapper, object, response);
            return;
        }
        writeBody(objectMapper, model, response);
    }

    private boolean hasOneModel(final Map<String, ?> model) {
        return model.size() == 1;
    }

    private Object extractFirstValue(final Map<String, ?> model) {
        final Object key = model.keySet().toArray()[0];
        final Object object = model.get(key);
        return object;
    }

    private void writeBody(final ObjectMapper objectMapper, final Object object, final HttpServletResponse response)
            throws IOException {
        final String json = objectMapper.writeValueAsString(object);
        response.getWriter().write(json);
    }
}
