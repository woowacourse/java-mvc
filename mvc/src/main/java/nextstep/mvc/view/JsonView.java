package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.util.Map;

public class JsonView implements View {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        response.getWriter().write(modelToJson(model));
    }

    private String modelToJson(Map<String, ?> model) throws JsonProcessingException {
        if(model.size() == 1) {
            objectMapper.writeValueAsString(model.values().toArray()[0]);
        }

        return objectMapper.writeValueAsString(model);
    }
}
