package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.mvc.exception.EmptyModelException;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String EMPTY = "";

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final PrintWriter printWriter = response.getWriter();
        try {
            printWriter.print(objectMapper.writeValueAsString(getAttribute(model)));
        } catch (EmptyModelException e) {
            printWriter.print(EMPTY);
        }
    }

    private Object getAttribute(Map<String, ?> model) {
        if (model.size() > 1) {
            return model;
        }
        return model.values()
                .stream()
                .findAny()
                .orElseThrow(EmptyModelException::new);
    }
}
