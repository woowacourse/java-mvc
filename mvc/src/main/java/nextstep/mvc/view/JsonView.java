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
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String responseBody = getResponseBody(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(responseBody);
    }

    private String getResponseBody(Map<String, ?> model) throws JsonProcessingException {
        if (hasOnlyOneValue(model)) {
            return model.values().toArray()[0].toString();
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }

    private boolean hasOnlyOneValue(Map<String, ?> model) {
        return model.size() == 1;
    }
}
