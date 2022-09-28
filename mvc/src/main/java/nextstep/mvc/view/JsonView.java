package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.Header;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        objectMapper = new JsonMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String body = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(getJsonBodyTarget(model));
        response.setHeader(Header.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter()
                .write(body);
    }

    private Object getJsonBodyTarget(final Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().toArray()[0];
        }
        return model;
    }
}
