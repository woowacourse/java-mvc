package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");

        final Object modelObject = modelToObject(model);

        final String jsonBody = objectMapper.writeValueAsString(modelObject);
        response.getWriter().append(jsonBody);
    }

    private Object modelToObject(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().stream()
                    .findFirst()
                    .orElseThrow();
        }

        return model;
    }
}
