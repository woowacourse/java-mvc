package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final String EMPTY_STRING = "";
    private static final int MAP_MIN_SIZE = 1;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String messageBody = toJson(model);
        response.getWriter().write(messageBody);
    }

    private String toJson(final Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            return EMPTY_STRING;
        }
        if (model.size() == MAP_MIN_SIZE) {
            return mapper.writeValueAsString(findFirstModel(model));
        }
        return mapper.writeValueAsString(model);
    }

    private Object findFirstModel(final Map<String, ?> model) {
        return model.values()
                .stream()
                .findAny()
                .orElse(null);
    }
}
