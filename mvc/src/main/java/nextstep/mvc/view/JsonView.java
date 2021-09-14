package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        String jsonValue = convertToJsonValue(model);
        response.getWriter().write(objectMapper.writeValueAsString(jsonValue));
    }

    private String convertToJsonValue(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            Map.Entry<String, ?> entry = model.entrySet().iterator().next();
            String key = entry.getKey();
            return objectMapper.writeValueAsString(model.get(key));
        }
        return objectMapper.writeValueAsString(model);
    }
}
