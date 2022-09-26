package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        PrintWriter responseWriter = response.getWriter();
        responseWriter.write(toJsonFormat(model));
    }

    private String toJsonFormat(Map<String, ?> model) throws JsonProcessingException {
        ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();

        if (model.size() == 1) {
            return objectWriter.writeValueAsString(model.values().toArray()[0]);
        }
        return objectWriter.writeValueAsString(model);
    }
}
