package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String jsonString = OBJECT_MAPPER.writeValueAsString(filterObjectToJsonify(model));
        response.getWriter().write(jsonString);
    }

    private Object filterObjectToJsonify(Map<String, ?> model) {
        if (hasSingleObject(model)) {
            return model.values().iterator().next();
        }
        return model;
    }

    private boolean hasSingleObject(Map<String, ?> model) {
        return model.size() == 1;
    }
}
