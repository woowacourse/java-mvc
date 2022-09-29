package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int CRITERIA = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final PrintWriter printWriter = response.getWriter();
        if (model.size() == CRITERIA) {
            printWriter.write(OBJECT_MAPPER.writeValueAsString(findValue(model)));
            return;
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        printWriter.write(OBJECT_MAPPER.writeValueAsString(model));
    }

    private Object findValue(Map<String, ?> model) {
        return model.values()
                .stream()
                .findFirst()
                .get();
    }
}
