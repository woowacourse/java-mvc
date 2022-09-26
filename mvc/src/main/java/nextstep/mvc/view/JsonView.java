package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.util.Map;

public class JsonView implements View {
    private static final int SINGLE_MODEL_SIZE = 1;
    private static final int SINGLE_MODEL_INDEX = 0;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final String responseBody = convertJsonToString(model);
        response.getWriter().write(responseBody);
    }

    private String convertJsonToString(final Map<String, ?> model) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        if (model.size() == SINGLE_MODEL_SIZE) {
            return objectMapper.writeValueAsString(model.values().toArray()[SINGLE_MODEL_INDEX]);
        }
        return objectMapper.writeValueAsString(model);
    }
}
