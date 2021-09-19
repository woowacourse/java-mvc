package nextstep.mvc.view;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final int FIRST_INDEX = 0;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int SINGLE_MODEL_VALUE_SIZE = 1;

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(ObjectToJson(model));
    }

    private String ObjectToJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == SINGLE_MODEL_VALUE_SIZE) {
            Object value = model.values().toArray()[FIRST_INDEX];
            return OBJECT_MAPPER.writeValueAsString(value);
        }

        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
