package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class JsonView implements View {

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writerWithDefaultPrettyPrinter();

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json");

        final Object modelObject = modelToObject(model);
        final String jsonBody = OBJECT_WRITER.writeValueAsString(modelObject);
        response.getWriter().append(jsonBody);
    }

    private Object modelToObject(Map<String, Object> model) {
        if (model.size() == 1) {
            return model.values().iterator().next();
        }
        return model;
    }
}
