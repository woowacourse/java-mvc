package nextstep.mvc.view;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.mvc.exception.view.WritingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger LOG = LoggerFactory.getLogger(JsonView.class);

    private final JsonConverter jsonConverter;

    public JsonView() {
        this(new JsonConverter());
    }

    private JsonView(JsonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Override
    public void render(
        Map<String, ?> model,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);

        if (model.size() == 1) {
            writeWithValue(model, response);
            return;
        }
        writeWithModel(model, response);
    }

    private void writeWithValue(Map<String, ?> model, HttpServletResponse response) {
        for (Object value : model.values()) {
            write(response, jsonConverter.toJson(value));
        }
    }

    private void writeWithModel(Map<String, ?> model, HttpServletResponse response) {
        write(response, jsonConverter.toJson(model));
    }

    private void write(HttpServletResponse response, String value) {
        try {
            PrintWriter writer = response.getWriter();
            writer.write(value);

            LOG.debug("Write Result: {}", value);
        } catch (Exception e) {
            throw new WritingException();
        }
    }
}
