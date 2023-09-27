package webmvc.org.springframework.web.servlet.view;

import static web.org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        String json = convertToJson(model);
        response.getWriter().write(json);
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
    }

    private String convertToJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return objectMapper.writeValueAsString(model.values().iterator().next());
        }
        return objectMapper.writeValueAsString(model);
    }
}
