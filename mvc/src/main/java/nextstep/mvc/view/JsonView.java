package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.mvc.exception.ModelAttributeEmptyException;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final Object attributes = getAttributes(model);
        final String body = objectMapper.writeValueAsString(attributes);
        final PrintWriter writer = response.getWriter();
        writer.write(body);
    }

    private Object getAttributes(final Map<String, ?> model) {
        if (model.size() <= 1) {
            return model.values()
                    .stream()
                    .findAny()
                    .orElseThrow(ModelAttributeEmptyException::new);
        }

        return model;
    }
}
