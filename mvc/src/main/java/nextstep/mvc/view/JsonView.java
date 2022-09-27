package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (model.size() == 1) {
            write(getFirstObject(model), response);
            return;
        }
        write(model, response);
    }

    private Object getFirstObject(final Map<String, ?> model) {
        final String key = (String) model.keySet().toArray()[0];
        return model.get(key);
    }

    private void write(final Object model, final HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter()
                .write(objectMapper.writeValueAsString(model));
    }
}
