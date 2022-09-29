package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(toJsonString(model));
    }

    private String toJsonString(Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            return "";
        }
        if (model.size() == 1) {
            return MAPPER.writeValueAsString(model.values().toArray()[0]);
        }
        return MAPPER.writeValueAsString(model);
    }
}
