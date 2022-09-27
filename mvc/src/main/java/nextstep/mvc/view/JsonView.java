package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final int PLAIN_RETURN_COUNT = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String body = toJsonBody(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);
    }

    private String toJsonBody(final Map<String, ?> model) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        if (model.size() == PLAIN_RETURN_COUNT) {
            return objectMapper.writeValueAsString(extractOneValue(model));
        }
        return objectMapper.writeValueAsString(model);
    }

    private Object extractOneValue(final Map<String, ?> model) {
        return model.values()
                .stream()
                .findAny()
                .orElseThrow();
    }
}
