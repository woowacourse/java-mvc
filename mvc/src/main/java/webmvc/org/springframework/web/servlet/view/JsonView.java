package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int SINGLE_DATA_SIZE = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final String jsonBody = createJsonBody(model);
        response.getWriter().write(jsonBody);
    }

    private String createJsonBody(final Map<String, ?> model) throws JsonProcessingException {
        if(model.size() == SINGLE_DATA_SIZE) {
            final Object singleData = model.values().iterator().next();
            return objectMapper.writeValueAsString(singleData);
        }
        return objectMapper.writeValueAsString(model);
    }
}
