package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object attributes = getAttributes(model);
        final String responseBody = objectMapper.writeValueAsString(attributes);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(responseBody);
    }

    private Object getAttributes(Map<String, ?> model) {
        if (model.size() > 1){
            return model;
        }

        Optional<?> attributes = model.values()
                .stream()
                .findAny();

        if (attributes.isEmpty()) {
            return new HashMap<>();
        }
        return attributes.get();
    }
}
