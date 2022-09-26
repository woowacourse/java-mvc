package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final int EMPTY = 0;
    private static final int ONLY_ONE = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Object value = decideModelToSerialize(model);
        try {
            String serialized = mapper.writeValueAsString(value);
            response.getWriter().write(serialized);
        } catch (IOException e) {
            throw new RuntimeException("Fails on write model as json", e);
        }
    }

    private Object decideModelToSerialize(Map<String, ?> model) {
        if (model.size() == EMPTY) {
            return "";
        }
        if (model.size() == ONLY_ONE) {
            return model.values().toArray()[0];
        }
        return model;
    }

    @Override
    public String getName() {
        return null;
    }
}
