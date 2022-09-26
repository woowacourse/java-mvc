package nextstep.mvc.view;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final int SINGLE_MODEL_SIZE = 1;
    private static final int SINGLE_VALUE_INDEX = 0;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final String jsonData = getJsonData(model);

        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter()
                .write(jsonData);
    }

    private String getJsonData(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == SINGLE_MODEL_SIZE) {
            return objectMapper.writeValueAsString(getSingleValue(model));
        }

        return objectMapper.writeValueAsString(model);
    }

    private Object getSingleValue(final Map<String, ?> model) {
        return model.values()
                .toArray()[SINGLE_VALUE_INDEX];
    }
}
