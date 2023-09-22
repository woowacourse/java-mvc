package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private static final int FIRST_VALUE_INDEX = 0;
    private static final int SINGLE_RESPONSE_VALUE = 1;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = getResponseBody(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);
    }

    private String getResponseBody(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == SINGLE_RESPONSE_VALUE) {
            Object data = model.values().toArray()[FIRST_VALUE_INDEX];
            return String.valueOf(data);
        }

        return objectMapper.writeValueAsString(model);
    }
}
