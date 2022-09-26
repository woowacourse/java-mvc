package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(convertJson(model));
    }

    private String convertJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            ArrayList<?> objects = new ArrayList<>(model.values());
            Object object = objects.get(0);
            return objectMapper.writeValueAsString(object);
        }
        return objectMapper.writeValueAsString(model);
    }
}
