package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        final String jsonBody = convertModelToJson(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter()
                .write(jsonBody);
    }

    private String convertModelToJson(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() > 1) {
            return objectMapper.writeValueAsString(model);
        }

        if (model.size() == 1) {
            final Object value = extractSingleValue(model);
            return objectMapper.writeValueAsString(value);
        }

        return "";
    }

    private Object extractSingleValue(final Map<String, ?> model) {
        return model.values()
                .stream()
                .findAny()
                .get();
    }
}
