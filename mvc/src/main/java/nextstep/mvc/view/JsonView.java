package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int SINGLE_OBJECT_COUNT = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String jsonValue = toJsonValue(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter()
                .write(jsonValue);
    }

    private String toJsonValue(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == SINGLE_OBJECT_COUNT) {
            Object object = model.values()
                    .stream()
                    .findFirst()
                    .orElseThrow();
            return OBJECT_MAPPER.writeValueAsString(object);
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
