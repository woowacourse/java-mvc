package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writerWithDefaultPrettyPrinter();

    private static final int EMPTY = 0;
    private static final int ONE = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Object jsonModel = getJsonModel(model);
        response.getWriter().write(OBJECT_WRITER.writeValueAsString(jsonModel));
    }

    private Object getJsonModel(Map<String, ?> model) {
        if (model.size() == EMPTY) {
            return "";
        }

        if (model.size() == ONE) {
            return model.values().toArray()[0];
        }
        return model;
    }
}
