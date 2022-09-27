package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String EMPTY = "";

    private final String body;

    public JsonView() {
        this(EMPTY);
    }

    public JsonView(final String body) {
        this.body = body;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String body = OBJECT_MAPPER.writeValueAsString(getBody(model));
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().println(body);
    }

    private Object getBody(final Map<String, ?> model) {
        if (model.size() != 1) {
            return model;
        }
        return model.values().stream()
            .findFirst()
            .orElseThrow();
    }
}
