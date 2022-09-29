package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final int ONE_OBJECT_CONVERSION_CONDITION = 1;
    private static final int ONE_OBJECT_KEY_INDEX = 0;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (model.size() == ONE_OBJECT_CONVERSION_CONDITION) {
            write(response, OBJECT_MAPPER.writeValueAsString(toOneObject(model)));
            return;
        }

        write(response, OBJECT_MAPPER.writeValueAsString(model));
    }

    private Object toOneObject(final Map<String, ?> model) {
        List<String> keys = new ArrayList<>(model.keySet());
        return model.get(keys.get(ONE_OBJECT_KEY_INDEX));
    }

    private void write(final HttpServletResponse response, final String jsonBody) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.print(jsonBody);
    }
}
