package nextstep.mvc.view;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String json = toJson(model);
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(json);
    }

    private String toJson(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            final Object object = model.values().toArray()[0];
            return mapper.writeValueAsString(object);
        }
        return mapper.writeValueAsString(model);
    }
}
