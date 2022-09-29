package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter()
                .write(getBody(model));
    }

    private String getBody(final Map<String, ?> model) throws JsonProcessingException {
        if (isSizeOne(model)) {
            return getBodyByOneModel(model);
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }

    private boolean isSizeOne(final Map<String, ?> model) {
        return model.size() == 1;
    }

    private String getBodyByOneModel(final Map<String, ?> model) throws JsonProcessingException {
        final Object object = model.values()
                .stream()
                .findFirst()
                .orElseThrow();
        return OBJECT_MAPPER.writeValueAsString(object);
    }
}
