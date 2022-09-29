package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.mvc.exception.EmptyModelException;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final int DEFAULT_FORMAT_SIZE = 1;
    private static final String EMPTY_MODEL_JSON_VALUE = "";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        try {
            Object attributes = getAttributes(model);
            writer.print(objectMapper.writeValueAsString(attributes));
        } catch (EmptyModelException e) {
            writer.print(EMPTY_MODEL_JSON_VALUE);
        }
    }

    private Object getAttributes(final Map<String, ?> model) {
        if (model.size() > DEFAULT_FORMAT_SIZE) {
            return model;
        }
        return model.values()
                .stream()
                .findAny()
                .orElseThrow(EmptyModelException::new);
    }
}
