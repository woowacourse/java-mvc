package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String responseBody = getResponseBody(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(responseBody);
    }

    private String getResponseBody(Map<String, ?> model) throws JsonProcessingException {
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        if (model.size() == 1) {
            Object value = model.values().iterator().next();
            return objectWriter.writeValueAsString(value);
        }
        return objectWriter.writeValueAsString(model);
    }
}
