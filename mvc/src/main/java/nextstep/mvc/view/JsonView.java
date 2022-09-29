package nextstep.mvc.view;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        writeAsJson(model, response);
    }

    private void writeAsJson(final Map<String, ?> model, final HttpServletResponse response) throws IOException {
        final var printWriter = response.getWriter();
        final var jsonString = valueAsString(model);
        printWriter.write(jsonString);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private String valueAsString(final Map<String, ?> model) throws JsonProcessingException {
        final var objectWriter = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(model);
    }
}
