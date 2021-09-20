package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Map<String, Object> model;

    public JsonView() {
        this(new HashMap<>());
    }

    public JsonView(Map<String, Object> model) {
        this.model = new HashMap<>(model);
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        this.model.putAll(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        printByModelSize(this.model, writer);
    }

    private void printByModelSize(Map<String, ?> model, PrintWriter writer)
        throws JsonProcessingException {
        if (model.entrySet().size() == 1) {
            final Object value = model.values().toArray()[0];
            writer.print(OBJECT_MAPPER.writeValueAsString(value));
            return;
        }
        writer.println(OBJECT_MAPPER.writeValueAsString(model));
    }
}
