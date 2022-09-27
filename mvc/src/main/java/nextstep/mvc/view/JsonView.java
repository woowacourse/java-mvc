package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(toJson(model));
    }

    private String toJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return OBJECT_MAPPER.writeValueAsString(getFirstValue(model));
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }

    private Object getFirstValue(Map<String, ?> model) {
        return model.keySet()
                .stream()
                .map(model::get)
                .findFirst()
                .orElseThrow();
    }
}
