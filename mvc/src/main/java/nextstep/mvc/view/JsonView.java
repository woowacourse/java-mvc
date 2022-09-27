package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final int SINGLE_MODEL_SIZE = 1;
    private static final int SINGE_VALUE_INDEX = 0;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        String body = getBody(model);
        writer.write(body);
    }

    private String getBody(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == SINGLE_MODEL_SIZE) {
            return objectMapper.writeValueAsString(getSingleValue(model));
        }
        return objectMapper.writeValueAsString(model);
    }

    private Object getSingleValue(final Map<String, ?> model) {
        return model.values()
            .toArray()[SINGE_VALUE_INDEX];
    }
}
