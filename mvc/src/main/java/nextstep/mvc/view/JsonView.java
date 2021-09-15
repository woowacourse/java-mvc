package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final int VALID_MODEL_SIZE = 1;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String jsonValue = convertToJsonValue(model);
        response.getWriter().write(objectMapper.writeValueAsString(jsonValue));
    }

    private String convertToJsonValue(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == VALID_MODEL_SIZE) {
            Map.Entry<String, ?> entry = model.entrySet().iterator().next();
            String key = entry.getKey();
            return objectMapper.writeValueAsString(model.get(key));
        }
        return objectMapper.writeValueAsString(model);
    }
}